This is guide is meant for the setup of a virtual machine with an apache server that shows an php dummy page, it is important to notice that this will be done with a NAT adapter instead of a bridge adpater due to the configuration of the network I live in (does not allow any simulated MAC adress presumably), so the idea will be to change the port that the apache server listens and exposes the app and then also map the ports in the nat adapter so we can access the web app from the host machine (my computer).

Interesting video:
https://www.youtube.com/watch?v=d7Kkbyb1TjQ&ab_channel=KeepItTechie

### 1. **Set Up the Ubuntu VM**:

Install Virtual Vox:
https://www.virtualbox.org/

Download image, in this case we will use Ubuntu 18.04.6:
https://releases.ubuntu.com/18.04/
download the ubuntu-18.04.6-live-server-amd64.iso


Create the virtual machine, that is straightforward, remember the user and password.
IMPORTANT:
Configure the first adapter under network as NAT (remember this is for using the same network for both the vm and the host machine).
![[Pasted image 20241109201448.png]]

Go thought the installation of the ubuntu server all with the default settings and just skip until you reach to the terminal:
![[Pasted image 20241109201816.png]]

### 2. **Install Apache and PHP**:

After setting up the Ubuntu system, you need to install Apache and PHP. Open the terminal in your Ubuntu VM and run the following commands:

```bash
sudo apt update 
sudo apt install apache2 
sudo apt install php libapache2-mod-php
```

- The `apache2` package installs the Apache HTTP server.
- The `php` package installs PHP.
- The `libapache2-mod-php` package integrates PHP with Apache.

### 3. **Create a Dummy PHP Application**:

Next, create a basic PHP application to serve as your dummy app. Here's a simple example.

- Go to the Apache document root directory:
```bash
cd /var/www/html
```
- Create a new PHP file:
```bash
sudo nano index.php
```
- Add the following content to the `index.php` file:
```bash
<?php 
echo "Hello, this is a PHP dummy app!"; 
?>
```
- Save and close the file.

- Remove any index.html if there are conflicts with our index.php
```bash
sudo rm index.html
```

### 4. **Start Apache**:
-------------
Something important here is that if we want to avoid any conflicts with the current ports used in the host machine, for example you couldn have already something running on port 80 or 8080, so we will change the port of the apache server in the VM so then later we can forward that to the host machine can use that to see the php page.

**Steps to change the Apache port:**

**Choose a Different Port**: 
- Pick a port that is not already in use by your host system (e.g., `8081`, `8888`, `9000`, etc.).
- **Update the Apache Configuration in the VM**:
	- Edit the `ports.conf` file:
```bash
sudo nano /etc/apache2/ports.conf
```
	- Change the `Listen` directive to your new port (e.g., `8888`)
```bash
Listen 8888
```
	- Edit the virtual host configuration:
```bash
sudo nano /etc/apache2/sites-available/000-default.conf
```
        `sudo nano /etc/apache2/sites-available/000-default.conf`
	  - Change the `<VirtualHost>` directive to your new port:
```bash
<VirtualHost *:8888>
```

 - **Restart Apache**: After saving the changes, restart Apache:
```bash
sudo systemctl restart apache2
```
--------


- **Start Apache** (if it's not already running):
```bash
sudo systemctl start apache2
```
- **Verify Apache is running**:
```bash
sudo systemctl status apache2
```

Extra:
if you don't see the message, check Apache's status to ensure it is running correctly. You can also view the Apache logs for errors:
```bash
sudo tail -f /var/log/apache2/error.log
```
### 5. **Set up Port Forwarding in VirtualBox**:

##### 1. **Port Forwarding in VirtualBox**:
- Open **VirtualBox**.
- Select your **Ubuntu VM** and click **Settings**.
- Go to the **Network** tab.
- Under **Adapter 1**, make sure **Attached to** is set to **NAT**.
- Click on **Advanced** to expand the settings.
- Click on **Port Forwarding**.

In the **Port Forwarding Rules** window, add a new rule with the following details:
- **Name**: `Apache`
- **Protocol**: `TCP`
- **Host Port**: `8888` (this is the port on your host machine you will use to access the server).
- **Guest Port**: `8888` (this is the port where Apache is running inside the VM).

![[Pasted image 20241109205320.png]]

**Note**: The `Host IP` is usually `127.0.0.1` (localhost), and the `Guest IP` is typically an IP in the `10.0.2.x` range for NAT, but you can leave the `Guest IP` blank and VirtualBox will handle it.

##### 2. **Restart Apache**:
- Once the port forwarding is set, make sure Apache is running inside your VM. If it's not running, start or restart it:
```bash
sudo systemctl restart apache2
```
### 6. **How to check status of server**
##### 1. **Check Apache Status**:
You can check if Apache is running with the following command:
```bash
sudo systemctl status apache2
```
If Apache is running, you will see an output similar to this:

● apache2.service - The Apache HTTP Server
   Loaded: loaded (/lib/systemd/system/apache2.service; enabled; vendor preset: enabled)
   Active: active (running) since Sat 2024-11-09 10:00:00 UTC; 1h 23min ago
     Docs: https://httpd.apache.org/docs/2.4/
  Process: 1234 ExecStart=/usr/sbin/apache2 -k start (code=exited, status=0/SUCCESS)
 Main PID: 1235 (apache2)
    Tasks: 55 (limit: 4653)
   Memory: 5.8M
   CGroup: /system.slice/apache2.service
           ├─1235 /usr/sbin/apache2 -k start
           ├─1236 /usr/sbin/apache2 -k start
           └─1237 /usr/sbin/apache2 -k start

### 7. **How to stop server**

To stop the Apache server on your Ubuntu VM, use the following command:
```bash
sudo systemctl stop apache2
```
This command stops the Apache service, meaning the PHP application will no longer be accessible via the browser until you restart the server.

If you want to **restart** the server (for example, after making changes to configuration files), use:
```bash
sudo systemctl restart apache2
```
And if you want to **disable** Apache from starting automatically on boot:
```bash
sudo systemctl disable apache2
```



### RESULT:

![[Pasted image 20241109205910.png]]
#### Bash script to setup apache server and php:
In the case of use for EC2 in AWS:
You can add this script (or just the commands you need) to the EC2 **User Data** section when launching the instance. User Data runs as the root user, so `sudo` may not be required.
```bash
#!/bin/bash
# Update package lists
apt-get update -y
# Install Apache and PHP with required modules
apt-get install -y apache2 php libapache2-mod-php
# Remove existing index.html or index.php if they exist
for file in /var/www/html/index.html /var/www/html/index.php; do
    if [ -f "$file" ]; then
        rm "$file"
        echo "$file has been removed to avoid conflicts."
    fi
done

# Define the path for the dummy PHP file
file_name="/var/www/html/index.php"
# Create the PHP file with styled content
echo "Creating $file_name with the info PHP basic page."
cat <<EOL > "$file_name"
<?php phpinfo(); ?>
EOL

# Restart Apache to apply changes
systemctl restart apache2

echo "Setup complete."

```