### Project diagrams
![[Pasted image 20241121200352.png]]

## **- Project structure**
Create directory where the files will be located:
/labAuthentication

The below files already exists in the following path if you don't want to create them from scratch.
documentation/CloudComputing/k8s-files/authentication_keycloak

**Structure of the project:**
.
├── certs
│     ├── localhostcert
│     ├── localhostcert.pem
│     └── localhostkey.pem
├── Dockerfile
├── Makefile
├── LabAuthentication
│     └──ANDROID CONTENT
├── node-api
│   ├── index.js
│   ├── node_modules
│   │   └── ALL NODE MODULES
│   ├── src
│   │   └── NODE APP CONTENT
│   ├── package-lock.json
│   └── package.json
├── scripts
│   ├──create_keycloak_client.ps1
│   └──seed.sql
├── pod.yaml
└── service.yaml

## **- Create files**

Create all the files needed for the lab:

Change directory to the certs folder
create the certificate with the below command:
Please note that we changed ip in the command to 10.151.130.198 that is the localhost of kike
run the below command to create the certificates:
```
openssl req -newkey rsa:2048 -nodes -keyout localhostkey.pem -x509 -days 365 -out localhostcert.pem -subj '/CN=test.keycloak.org/O=Test Keycloak./C=US' -extensions EXT -config <( \
echo -e "[dn]\nCN=test.keycloak.org\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:internal.oceloti.com,IP:10.151.130.198\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth")
```
Create the certificate for the node app and put it on the node-api folder.
```bash
openssl req -newkey rsa:2048 -nodes -keyout nodeappkey.pem -x509 -days 365 -out nodeappcert.pem -subj '/CN=test.nodeapp.local/O=Test Node App./C=US' -extensions EXT -config <( \
echo -e "[dn]\nCN=test.nodeapp.local\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:nodeapp.local,IP:10.151.130.198\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth")
```


Get the pin for the Android App.
```bash
openssl x509 -in localhostcert.pem -pubkey | \ openssl pkey -pubin -outform DER | \ openssl dgst -sha256 -binary | \ base64
```

**Makefile (needed to automize the creation and deletion of the pod)**
```makefile
# Define variables
DOCKER_IMAGE_KEYCLOAK=custom-keycloak:latest
DOCKER_IMAGE_NODE=node-api:latest

POD_FILES=pod.yaml service.yaml

# Build Keycloak Docker image
build-keycloak:
	docker build -t $(DOCKER_IMAGE_KEYCLOAK) .

# Build Node.js Docker image
build-node:
	docker build -t $(DOCKER_IMAGE_NODE) $(NODE_API_PATH)

# Apply Kubernetes configurations
up: build-keycloak
	kubectl create secret tls my-tls-secret   --cert=certs/localhostcert.pem   --key=certs/localhostkey.pem
	kubectl apply -f pod.yaml -f service.yaml

# Delete Kubernetes resources
down:
	kubectl delete secret my-tls-secret
	kubectl delete secret keycloak-client-secret
	kubectl delete  -f pod.yaml -f service.yaml

# Clean up dangling Docker images
clean:
	docker system prune -f

# Populate the database
seed-db:
	kubectl exec -it postgres-pod -- psql -U keycloak -d keycloak -c "/scripts/seed.sql"
```
**Keycloak Dockerfile:**
**Dockerfile**
```docker
# Use the official Keycloak image as the base
FROM quay.io/keycloak/keycloak:latest as builder

#Change this ip with your host machine either windows or macos
ENV HOST_MACHINE=10.151.130.198

# Enable health and metrics
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Configure database vendor
ENV KC_DB=postgres

WORKDIR /opt/keycloak

# Optimize the Keycloak build
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:latest
COPY --from=builder /opt/keycloak/ /opt/keycloak/

# Set environment variables for PostgreSQL and Keycloak
ENV KC_DB=postgres
ENV KC_DB_URL=jdbc:postgresql://postgres-service:5432/keycloak
ENV KC_DB_USERNAME=keycloak
ENV KC_DB_PASSWORD=mypassword
ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin
ENV KC_BOOTSTRAP_ADMIN_PASSWORD=admin
ENV KC_HOSTNAME=https://${HOST_MACHINE}:32080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
CMD ["start", "--optimized"]
```

**File including the pod definition:**
**pod.yaml**
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: postgres-pod
  labels:
    app: postgres-pod
spec:
  containers:
    - name: postgres
      image: postgres:latest
      env:
        - name: POSTGRES_USER
          value: "keycloak"
        - name: POSTGRES_PASSWORD
          value: "mypassword"
        - name: POSTGRES_DB
          value: "keycloak"
      ports:
        - containerPort: 5432
---
apiVersion: v1
kind: Pod
metadata:
  name: mailhog-pod
  labels:
    app: mailhog-pod
spec:
  containers:
    - name: mailhog
      image: mailhog/mailhog:latest
      ports:
        - containerPort: 1025 # SMTP Port
        - containerPort: 8025 # Web UI Port
---
apiVersion: v1
kind: Pod
metadata:
  name: keycloak-pod
  labels:
    app: keycloak-pod
spec:
  containers:
    - name: keycloak
      image: custom-keycloak:latest
      imagePullPolicy: IfNotPresent # Use local image if available
      env:
        - name: KC_DB
          value: "postgres"
        - name: KC_DB_URL
          value: "jdbc:postgresql://postgres-service:5432/keycloak"
        - name: KC_DB_USERNAME
          value: "keycloak"
        - name: KC_DB_PASSWORD
          value: "mypassword"
        - name: KC_BOOTSTRAP_ADMIN_USERNAME
          value: "admin"
        - name: KC_BOOTSTRAP_ADMIN_PASSWORD
          value: "admin"
        - name: KC_HOSTNAME
          value: "10.151.130.198"
        - name: KC_HTTPS_PORT
          value: "8443" # Explicitly configure HTTPS port
        - name: KC_HTTPS_CERTIFICATE_FILE
          value: "/opt/keycloak/demo/certs/tls.crt"
        - name: KC_HTTPS_CERTIFICATE_KEY_FILE
          value:  "/opt/keycloak/demo/certs/tls.key"
        - name: KC_EMAIL_HOST
          value: "mailhog-service"  # Name of the MailHog service
        - name: KC_EMAIL_PORT
          value: "1025"             # MailHog SMTP port
        - name: KC_EMAIL_FROM
          value: "no-reply@test.keycloak.org"  # Sender email address
        - name: KC_EMAIL_AUTH
          value: "false"            # MailHog does not require authentication
      ports:
        - containerPort: 8443
      volumeMounts:
      - name: certsvol
        mountPath: /opt/keycloak/demo/certs
  volumes:
  - name: certsvol
    secret:
      secretName: my-tls-secret
```
Service to configure the ports:
service.yaml
```yaml
apiVersion: v1
kind: Service
metadata:
  name: keycloak-service
spec:
  selector:
    app: keycloak-pod
  ports:
    - protocol: TCP
      port: 8443
      targetPort: 8443
      nodePort: 32080
      name: keycloak-https-port 
  type: NodePort
---
apiVersion: v1
kind: Service
metadata:
  name: postgres-service
spec:
  selector:
    app: postgres-pod
  ports:
    - protocol: TCP
      port: 5432
      targetPort: 5432
      nodePort: 32081
      name: postgres-port
  type: NodePort
---
apiVersion: v1
kind: Service
metadata:
  name: mailhog-service
spec:
  selector:
    app: mailhog-pod
  ports:
    - protocol: TCP
      port: 1025 # SMTP Port
      targetPort: 1025
      nodePort: 32025
      name: smtp-port
    - protocol: TCP
      port: 8025 # Web UI Port
      targetPort: 8025
      nodePort: 32026
      name: web-ui-port
  type: NodePort
```

For the node application we will need this:
**You need to run the following command to have the setup correctly of the node app**
```bash
npm install
```

We have to also change the client secret and add it to the .env that will come later in this docu.


## **- Install make in your WSL (ubuntu in windows in my case)**
Install make if not already installed (this will help to run the Makefile with the automation for starting the pod of kubernetes):
```bash
apt install make
```

###  Install JQ in ubuntu
```bash
sudo apt-get install -y jq
```
### Install JQ in Windows
https://jqlang.github.io/jq/download/
```powershell
winget install jqlang.jq
```

## **- Docker Desktop + Kubernetes**
Start Docker Desktop and start the Kubernetes service.
![[Pasted image 20241120151756.png]]

## **- Run the Makefile**
For starting you need to use the following command:
```bash
make up
```

and for removing the pod you need the following:
```bash
make down
```

By the way the images could still be there so if you want you can delete them.

## **-  Check if pod creation worked right**

```bash
kubectl get pods
```
```bash
kubectl get services
```
```bash
kubectl logs <pod-name>
```

## **-Create the Keycloak clients**

Once keycloak is up and running under:
https://10.151.130.198:32080/

##### Create the REALM oauthrealm
Access with these credentials
user: **admin**
password: **admin**
![[Pasted image 20241206191813.png]]
##### Create admin user for oauthrealm

Username: admin
Email: admin@gmail.com
First name: admin
Last name: admin

Credentials: admin (No temporary)

Role mapping:
- realm-management   realm-admin
- default-roles-oauthrealm

##### Create the client "node-api" and enable secrets this for the node app:
To create need to be under windows as:
```powershell
.\create_keycloak_client.ps1
```
```powershell
# Disable SSL certificate validation
[System.Net.ServicePointManager]::ServerCertificateValidationCallback = { $true }

# Set Keycloak URL based on environment
if ($env:COMPUTERNAME -like "*keycloak*") {
    # Internal cluster communication
    $KeycloakUrl = "https://keycloak-service:8443/auth"
} else {
    # External host communication (NodePort)
    $KeycloakUrl = "https://10.151.130.198:32080"
}

# Environment variables
$Realm = "oauthrealm"
$AdminUser = "admin"
$AdminPass = "admin"
$ClientId = "node-api"

# Delete existing secret if it exists
Write-Host "Checking if the secret exists..."
$SecretExists = kubectl get secret keycloak-client-secret -o json 2>$null
if ($?) {
    Write-Host "Deleting existing secret..."
    kubectl delete secret keycloak-client-secret
}


# Authenticate with Keycloak
Write-Host "Requesting admin token from $KeycloakUrl..."
$TokenResponse = Invoke-RestMethod -Method POST -Uri "$KeycloakUrl/realms/$Realm/protocol/openid-connect/token" `
    -Headers @{ "Content-Type" = "application/x-www-form-urlencoded" } `
    -Body @{ "username" = $AdminUser; "password" = $AdminPass; "grant_type" = "password"; "client_id" = "admin-cli" }

if (-not $TokenResponse.access_token) {
    Write-Host "Failed to retrieve admin token. Check Keycloak URL or admin credentials." -ForegroundColor Red
    exit 1
}

$Token = $TokenResponse.access_token

# Check if the client already exists
Write-Host "Checking if client '$ClientId' exists..."
$ClientResponse = Invoke-RestMethod -Method GET -Uri "$KeycloakUrl/admin/realms/$Realm/clients?clientId=$ClientId" `
    -Headers @{ "Authorization" = "Bearer $Token"; "Content-Type" = "application/json" }

if ($ClientResponse.Count -eq 0) {
    Write-Host "Client '$ClientId' does not exist. Creating it..."
    Invoke-RestMethod -Method POST -Uri "$KeycloakUrl/admin/realms/$Realm/clients" `
        -Headers @{ "Authorization" = "Bearer $Token"; "Content-Type" = "application/json" } `
        -Body (@{
            clientId = $ClientId
            enabled = $true
            protocol = "openid-connect"
            publicClient = $false
            serviceAccountsEnabled = $true
        } | ConvertTo-Json -Depth 10)
} else {
    Write-Host "Client '$ClientId' already exists."
}

# Fetch the client ID
$ClientIdResponse = $ClientResponse | Where-Object { $_.clientId -eq $ClientId }
$ClientIdUUID = $ClientIdResponse.id

if (-not $ClientIdUUID) {
    Write-Host "Failed to retrieve client ID for '$ClientId'." -ForegroundColor Red
    exit 1
}

# Fetch the client secret
Write-Host "Fetching client secret for '$ClientId'..."
$SecretResponse = Invoke-RestMethod -Method GET -Uri "$KeycloakUrl/admin/realms/$Realm/clients/$ClientIdUUID/client-secret" `
    -Headers @{ "Authorization" = "Bearer $Token"; "Content-Type" = "application/json" }

if (-not $SecretResponse.value) {
    Write-Host "Failed to retrieve client secret." -ForegroundColor Red
    exit 1
}

$ClientSecret = $SecretResponse.value
Write-Host "Client Secret for '$ClientId': $ClientSecret"

# Save the client secret as a Kubernetes secret
Write-Host "Saving client secret to Kubernetes..."
kubectl create secret generic keycloak-client-secret --from-literal=client-secret=$ClientSecret --dry-run=client -o yaml | kubectl apply -f -
```

To delete if you want (alredy included with make down)
```bash
kubectl delete secret keycloak-client-secret
```

Create if you get the secret manually:
```bash
kubectl create secret generic keycloak-client-secret \
  --from-literal=client-secret=<secret>
```

##### Create the client manually for the mobile app this is without secrets

Client creation wizard:
Client type: OpenID Connect
CLIENT ID: lab-authentication-client
Client authentication: off
Authentication flow: Standard flow, Direct access grants
Valid redirect URIs: https://10.151.130.198/oauth2redirect

Once created:
Access Token Lifespan : Expires in 7 Minutes
Proof Key for Code Exchange Code Challenge Method: S256

### **- Setting Up PostgreSQL Tables**

##### **Option A. Connecting to PostgreSQL in the Pod**

To initialize the database with tables and pre-filled data:

1. **Access the PostgreSQL Pod**:
```bash
kubectl exec -it postgres-pod -- psql -U keycloak -d keycloak
```

2. **Create the Tables**: Run the following SQL script inside the PostgreSQL shell:

```sql
-- Create the stores table 
CREATE TABLE stores ( 
store_id SERIAL PRIMARY KEY, 
name VARCHAR(255) NOT NULL, 
location VARCHAR(255) NOT NULL ); 
-- Create the sales table 
CREATE TABLE sales ( 
sale_id SERIAL PRIMARY KEY, 
user_id VARCHAR(255) NOT NULL, 
product_id INT NOT NULL, 
status VARCHAR(50) NOT NULL, 
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ); 
-- Insert sample data into stores 
INSERT INTO stores (name, location) VALUES 
('Candy Shop', '123 Sweet St'), 
('Cake House', '456 Dessert Dr'), 
('Choco World', '789 Cocoa Ln');

GRANT CONNECT ON DATABASE keycloak TO keycloak;
```
3.  **Exit the Shell**:
    `\q`

##### **Option B. Using seed.sql script**

To copy the script to the postgres pod:
```bash
kubectl cp seed.sql postgres-pod:/seed.sql
```
Enter the pod with bash interface:
```bash
kubectl exec -it postgres-pod -i bash
```
Run the script from inside the pod:
```bash
psql -U keycloak -d keycloak -f /seed.sql
```
If want to acces the plsql use:
```bash
psql -U keycloak -d keycloak
```
Grant access to keycloak user:
```bash
GRANT CONNECT ON DATABASE keycloak TO keycloak;
```

## **- Nexus implementation**
To get the admin password:
```bash
 ls -ld /var/jenkins_home/workspace/Build_Android_and_Upload_to_Nexus

```
```bash
cat /nexus-data/admin.password
```
Result example:
98a0b8b1-9021-4271-8b5f-9caa947ef0cfbash

Then use the password without bash and the user as admin:
Ej.
user: admin
password: 98a0b8b1-9021-4271-8b5f-9caa947ef0cf
change password to: admin

##### **1. Configure a Repository in Nexus**
In Nexus, we need to create a repository to store the Android build artifacts.
#### Steps to Create a Repository:
1. **Log into Nexus** at `http://localhost:32084/`.
2. Go to **Repositories** > **Create Repository**.
3. Choose **maven2 (hosted)** as the repository type (or raw, depending on how you plan to upload the APKs).
4. Configure the repository:
    - **Repository Name**: `android-builds`
    - **Version Policy**: Release
    - **Deployment policy**: Allow redeploy
    - Leave other settings as default.
5. Save the repository.

## **- Jenkins implementation**

Job name:
Build_Android_and_Upload_to_Nexus
Type: Pipeline

Script:
```
pipeline {
    agent any

    environment {
        ANDROID_HOME = '/usr/local/android-sdk'
        KEYSTORE_PATH = 'release-key.jks'
        KEYSTORE_PASSWORD = credentials('keystore-password') // Add the password as a Jenkins credential
        KEY_ALIAS = credentials('key-alias') // Add the alias as a Jenkins credential
        KEY_PASSWORD = credentials('key-password') // Add the key password as a Jenkins credential
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/luismendezc/documentation.git'
            }
        }
        stage('Prepare Keystore') {
            steps {
                withCredentials([file(credentialsId: 'android-release-keystore', variable: 'KEYSTORE_FILE')]) {
                    sh """
                    cp \$KEYSTORE_FILE $WORKSPACE/release-key.jks
                    """
                }
            }
        }
        stage('Build Android Project') {
            steps {
                sh """
                cd CloudComputing/k8s-files/authentication_keycloak/LabAuthentication
                chmod +x ./gradlew
                ./gradlew clean assembleRelease -Pandroid.injected.signing.store.file=$WORKSPACE/release-key.jks \
                  -Pandroid.injected.signing.store.password=$KEYSTORE_PASSWORD \
                  -Pandroid.injected.signing.key.alias=$KEY_ALIAS \
                  -Pandroid.injected.signing.key.password=$KEY_PASSWORD
                """
            }
        }
        stage('Verify Signed APK') {
            steps {
                sh """
                if [ ! -f "CloudComputing/k8s-files/authentication_keycloak/LabAuthentication/app/build/outputs/apk/release/app-release.apk" ]; then
                  echo "Signed APK not found!"
                  exit 1
                fi
                """
            }
        }
        stage('Upload to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: 'nexus-service:8081',
                    repository: 'android-builds',
                    credentialsId: 'nexus-admin', // Replace with the actual credentials ID
                    groupId: 'com.oceloti.lemc.labauthentication',
                    version: '1.0.0',
                    artifacts: [[
                        artifactId: 'app-release',
                        classifier: '',
                        file: 'CloudComputing/k8s-files/authentication_keycloak/LabAuthentication/app/build/outputs/apk/release/app-release.apk',
                        type: 'apk'
                    ]]
                )
            }
        }

    }
}

```

#### **- Configure Jenkins Credentials**

To allow Jenkins to authenticate with Nexus, create a credential in Jenkins:

1. Go to **Manage Jenkins** > **Manage Credentials**.
2. Add a new credential:
    - **Kind**: Username with password
    - **Username**: `admin` (or your Nexus username)
    - **Password**: (Your Nexus admin password)
    - **ID**: `nexus-admin`

#### **- Add the Keystore to Jenkins Credentials**

1. Open your Jenkins dashboard.
2. Go to **Manage Jenkins** > **Manage Credentials**.
3. Select the appropriate scope (e.g., Global credentials).
4. Click **Add Credentials**.
5. Choose **Secret file** as the credential type.
6. Upload your `release-key.jks` file.
7. Give it a meaningful **ID** (e.g., `android-release-keystore`).

#### **- Credentials for Keystore Passwords**
#### Add `keystore-password`:

1. Go to **Manage Jenkins** > **Manage Credentials** > **Global** > **Add Credentials**.
2. Choose **Secret text** as the credential type.
3. Fill in:
    - **ID**: `keystore-password`.
    - **Secret**: The keystore password.
#### Add `key-alias`:

1. Repeat the process above.
2. **ID**: `key-alias`.
3. **Secret**: The alias of your key in the keystore.
#### Add `key-password`:

1. Repeat the process above.
2. **ID**: `key-password`.
3. **Secret**: The password for the key.



## **- Android Implementation**
Look LabAuthentication inside documentation\CloudComputing\k8s-files\authentication_keycloak

Create Keystore:
```bash
keytool -genkeypair -v -keystore release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias keyAlias
```
- Save the `release-key.jks` file in a secure location.
- Note down the keystore password, key alias, and key password.

passwords: admin123


## **- Node JS Implementation**
Since the API is private, you'll need a Keycloak client that uses **client credentials** with a secret:

modify the .env file to use the correct values:

KEYCLOAK_URL=https://10.151.130.198:32080
KEYCLOAK_REALM=oauthrealm
CLIENT_ID=node-api
CLIENT_SECRET=<change_this>


POSTGRES_USER=keycloak
POSTGRES_PASSWORD=mypassword
POSTGRES_DB=keycloak
POSTGRES_HOST=10.151.130.198
POSTGRES_PORT=32081

Once the .env is ready then run this:
```bash
npm start
```
the server is on:
10.151.130.198:3000
### Simulate token access with node app:

```bash
curl -k -X POST "https://10.151.130.198:32080/realms/oauthrealm/protocol/openid-connect/token"   -H "Content-Type: application/x-www-form-urlencoded"   -d "client_id=node-api"   -d "client_secret=<secret>"   -d "grant_type=client_credentials"
```

```bash
curl -k -X GET "http://10.151.130.198:3000/api/secure" \
  -H "Authorization: Bearer <VALID_ACCESS_TOKEN>"
```

## IMPORTANT COMMANDS AND NOTICES
adb shell am start -W -a android.intent.action.VIEW -d "https://10.151.130.198/oauth2redirect"

openssl pkcs12 -export -in localhostcert.pem -inkey localhostkey.pem -out localhostcert.p12 -name "Localhost Certificate" -passout pass:your_password

Remember register the app link in the app settings very important

# Final result:

Postgres machine:
![[Pasted image 20241120152417.png]]

Keycloak machine (user: admin, password: admin):
![[Pasted image 20241120152535.png]]