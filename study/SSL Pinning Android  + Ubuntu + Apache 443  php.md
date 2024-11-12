Result:
![[Pasted image 20241112194040.png]]
Check LabSSLPinning in the android apps folder.

### 1. Follow the previous setup steps:
[[Setup apache server with php page in Virtual Box Machine and NAT adapter]]

### 2. Configure Redirect ports in the case of NAT
![[Pasted image 20241112194544.png]]

### 3. Setup 443 server
To set up your Apache server to run on the secure HTTPS port (443) using a self-signed certificate, you can follow these steps.

Ensure that the SSL module is enabled in Apache:
```bash
sudo a2enmod ssl 
sudo systemctl restart apache2
```
Create a place to store the certificates needed:
```bash
sudo mkdir /etc/apache2/ssl
```

**THIS IS IN THE CASE OF THE NAT CONFIGURATION OF VIRTUAL BOX**
**Create a Configuration File (`openssl.cnf`)**:
Create a file named `openssl.cnf` (or use an existing configuration file) with the following content:
```conf
[req] 
default_bits = 2048 
prompt = no 
default_md = sha256 
distinguished_name = dn 
req_extensions = req_ext 
x509_extensions = v3_ca # The extensions to add to the self-signed cert 

[dn] 
C = DE 
ST = Dresden 
L = Dresden 
O = oceloti 
OU = network 
CN = Luis 
emailAddress = lemendezc@hotmail.com 

[req_ext] 
subjectAltName = @alt_names 
[v3_ca] 
subjectAltName = @alt_names 
[alt_names] 
IP.1 = 10.151.130.198 # IP of the host machine you are running the VM
```

**Generate the Certificate**:
Use the `openssl` command to generate a new self-signed certificate using the configuration file:
(under /etc/apache2/ssl)
```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout server.key -out server.crt -config openssl.cnf
```
- `server.key` is the private key.
- `server.crt` is the self-signed certificate.

 **Update Apache Configuration Files**
- Open your Apache SSL configuration file. This might be located at `/etc/apache2/sites-available/default-ssl.conf` or another SSL-specific configuration file.
    
- Locate the lines that specify the paths to your current certificate and key files. It might look something like this:
```apache
SSLCertificateFile /etc/apache2/ssl/server.crt
SSLCertificateKeyFile /etc/apache2/ssl/server.key
```
Restart the Apache service to apply the changes:
```bash
sudo systemctl restart apache2
```


If you have modified the default SSL configuration, you can enable it using:

```bash
sudo a2ensite default-ssl 
sudo systemctl restart apache2
```

### 4. Create php /user endopoint to return json
Create a new file named `users.php` in the root directory of your web server (where your `index.php` is located). Maybe is located int /var/www/html/

- Add the PHP Code to `users.php`
In `users.php`, add the following code to return the JSON response:
```php
<?php 
// Set content type to JSON 
header('Content-Type: application/json'); 

// Create an array of users 
$users = [ 
	["id" => 1, "name" => "Alice", "age" => 30], 
	["id" => 2, "name" => "Bob", "age" => 25], 
	["id" => 3, "name" => "Charlie", "age" => 35] 
]; 
// Output the JSON-encoded data 
echo json_encode($users); 
?>
```

Create also in /var/www/html/ the `.htaccess` File and add the following content (this only works for this example you have to check more if you want something else):

```apache
RewriteEngine On
RewriteCond %{REQUEST_FILENAME} !-f
RewriteCond %{REQUEST_FILENAME} !-d
RewriteRule ^users$ users.php [L]
```
Ensure that the Apache `mod_rewrite` module is enabled. You can enable it with the following command:
```bash
sudo a2enmod rewrite 
sudo systemctl restart apache2
```

**Check `AllowOverride` Directive**
For `.htaccess` files to work, Apache must allow overrides in the directory configuration. You can verify this by checking the main Apache configuration file (`/etc/apache2/apache2.conf` on Ubuntu) or the virtual host configuration.

Look for a section like this:
```apache
<Directory /var/www/html> 
	Options Indexes FollowSymLinks 
	AllowOverride All 
	Require all granted 
</Directory>
```
Make sure your Apache server is running:
```bash
sudo systemctl restart apache2
```

### 5. Android code
Video that could be interesting regarding SSL Pinning:
https://www.youtube.com/watch?v=uSnDRcoYgqg&ab_channel=FIERYDINESH
##### **SETUP NEEDED:**
With the android code you will notice the network config file and we need the certificate from the server and also the pin to allow the communication.

For getting the files and so from the Virtual Machine we can connect to the VM from the Host using SSH and SCP:

**Check if SSH is installed and running**
First, make sure that the SSH server is installed and running on the Ubuntu VM.
To check the status of the SSH service, run:
```bash
sudo systemctl status ssh
```
If it is not active, start it with:
```bash
sudo systemctl start ssh
```

If SSH is not installed, you can install it using:
```bash
sudo apt update 
sudo apt install openssh-server
```
After installation, start the SSH service:
```bash
sudo systemctl start ssh 
sudo systemctl enable ssh
sudo systemctl restart ssh
```

**Para conectar con la máquina virtual:**
```bash
ssh lemendezc@127.0.0.1 -p 2222
```

**Para recibir y enviar con la máquina virtual:**
```bash
scp -P 2222 lemendezc@127.0.0.1:/etc/apache2/ssl/server.crt ./
```

##### **WHAT WE NEED TO DO:**
Generate the pin and replace this in the network config xml in android
```bash
openssl x509 -in /etc/apache2/ssl/server.crt -pubkey -noout | openssl rsa -pubin -outform DER | openssl dgst -sha256 -binary | base64
```

Copy the .crt from the virtual machine to the raw/my_cert.crt in Android:
```bash
scp -P 2222 lemendezc@127.0.0.1:/etc/apache2/ssl/server.crt ./
```
Then copy it on Android.



You should not get any Error on android and get the list of 3 persons that means the communication is correct. ;)