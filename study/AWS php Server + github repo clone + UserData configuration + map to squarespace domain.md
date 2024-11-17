Check the pdf to see the images showing every step:
![[AWS php Server.pdf]]

#### 1. Create the GitHub Repository for the PHP Dummy Page

- Create a GitHub repository to host your PHP web application files (e.g., `phpDummy`).
- Ensure the repository is private if you want to restrict public access.

Example:
https://github.com/luismendezc/phpDummy

#### 2. Create a Personal Access Token (PAT) for GitHub

- Go to your GitHub account settings.
- Navigate to "Developer settings" > "Personal access tokens".
- Generate a new token with appropriate repository access scopes.
- Note down the token securely.

#### 3. Create an AWS SSM Parameter for the Token

- Go to the AWS Management Console.
- Navigate to **AWS Systems Manager (SSM)** > **Parameter Store**.
- Create a new parameter with the following:
    - Name: `GitHubToken`
    - Type: `SecureString`
    - Value: (Your GitHub token)
    - Description: `GitHub PAT for EC2 automation`

#### 4. Create an AWS IAM Role for EC2 Access

- Navigate to **AWS IAM** > **Roles**.
- Create a new role for the EC2 service.
- Attach the policy `AmazonSSMReadOnlyAccess` to allow access to the SSM parameter.
- Name the role appropriately (e.g., `EC2SSMAccessRole`).
#### 5. Launch an EC2 Instance

- Go to the **AWS EC2 Dashboard**.
- Click on **Launch Instance**.
- Configure the instance:
    - **AMI:** Choose Ubuntu Free Tier.
    - **Instance Type:** Select a Free Tier-eligible instance.
    - **IAM Role:** Select the role you created earlier (`EC2SSMAccessRole`).
    - **User Data:** Use the following script to initialize the instance:

```bash
#!/bin/bash

# Update package list and install required packages
sudo apt-get update
sudo apt-get install -y apache2 php libapache2-mod-php git unzip curl jq

# Install AWS CLI using the official method to ensure compatibility
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Retrieve GitHub token from AWS Systems Manager Parameter Store
GITHUB_TOKEN=$(aws ssm get-parameter --name "GitHubToken" --with-decryption --query "Parameter.Value" --output text)

# Check if the token retrieval was successful
if [ -z "$GITHUB_TOKEN" ]; then
    echo "Failed to retrieve GitHub token from AWS SSM. Exiting."
    exit 1
fi

# Create the web root directory if it doesn't exist
sudo mkdir -p /var/www/html

# Navigate to the web root
cd /var/www/html || exit 1

# Remove index.html if it exists
if [ -f "index.html" ]; then
    sudo rm -f index.html
fi

# Clone the GitHub repository using the token retrieved from AWS SSM
sudo git clone https://$GITHUB_TOKEN@github.com/luismendezc/phpDummy.git || {
    echo "GitHub clone failed. Exiting."
    exit 1
}

# Check if the repository was cloned successfully
if [ ! -d "phpDummy" ]; then
    echo "Failed to clone repository. Exiting."
    exit 1
fi

# Move files to the web root and handle any conflicts
sudo mv phpDummy/public/* /var/www/html/ || {
    echo "Moving files failed. Exiting."
    exit 1
}

sudo cp -r phpDummy/assets /var/www/html/ || {
    echo "Copying assets failed. Exiting."
    exit 1
}

sudo cp -r phpDummy/templates /var/www/html/ || {
    echo "Copying templates failed. Exiting."
    exit 1
}

# Set the correct permissions
sudo chown -R www-data:www-data /var/www/html/
sudo chmod -R 755 /var/www/html/

# Restart Apache to apply changes
sudo systemctl restart apache2

# Clean up installation files
rm -f awscliv2.zip
rm -rf aws

echo "Setup complete."
```

- **Security Group Rules:**
    - Allow inbound traffic for **SSH (port 22)**.
    - Allow inbound traffic for **HTTP (port 80)**.
    - Allow inbound traffic for **HTTPS (port 443)**.

#### 6. Configure Security Group Network Rules

- Ensure the security group attached to the EC2 instance allows the following:
    - **SSH (port 22)** for remote access.
    - **HTTP (port 80)** for web traffic.
    - **HTTPS (port 443)** for secure traffic.

#### 7. Configure Subdomain in Squarespace

- Go to your Squarespace DNS settings.
- Add an **A Record** pointing to the public IP of your EC2 instance.
- Set the subdomain as `unir.oceloti.com`.

#### 8. Connect to the EC2 Instance Using SSH

- From your local machine, connect to the EC2 instance using the `.pem` key:
```bash
ssh -i "path-to-your-key.pem" ubuntu@your-ec2-public-ip
```

If you want to check the status of the setup made by the User data:
```bash
sudo cat /var/log/cloud-init-output.log
```

#### 9. Install Let’s Encrypt SSL Certificate

- Connect to the EC2 instance using SSH.
- Install Certbot
```bash
sudo apt-get install certbot python3-certbot-apache
```

- Generate and install the certificate:
```bash
sudo certbot --apache -d unir.oceloti.com
```
-  Follow the prompts to complete the setup.

#### 10. Configure Redirection from HTTP to HTTPS

- Open the Apache SSL configuration file:
```bash
sudo nano /etc/apache2/sites-available/000-default-le-ssl.conf
```
- Ensure the following lines are added (inside or outside the `<IfModule mod_ssl.c>` block):
```bash
<VirtualHost *:80> 
	ServerName unir.oceloti.com 
	Redirect permanent / https://unir.oceloti.com/ 
</VirtualHost>
```
- Save and exit the file.
- Restart Apache to apply changes:
```bash
sudo systemctl restart apache2
```
### Final Steps and Verification

- Access your site using the URL: [https://unir.oceloti.com](https://unir.oceloti.com).
- Verify that:
    - The site is accessible via HTTPS.
    - HTTP requests are redirected to HTTPS.
    - Your site content displays as expected