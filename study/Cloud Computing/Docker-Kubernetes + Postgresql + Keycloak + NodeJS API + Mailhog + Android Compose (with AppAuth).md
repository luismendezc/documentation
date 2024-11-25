### 0.Project diagrams
![[Pasted image 20241121200352.png]]

## **1. Project structure**
Create directory where the files will be located:
/labAuthentication

The below files already exists in the following path if you don't want to create them from scratch.
documentation/CloudComputing/k8s-files/authentication_keycloak

**Structure of the project:**
.
├── certs
├── Dockerfile
├── Makefile
├── ingress.yaml
├── node-api
│   ├── Dockerfile
│   ├── index.js
│   ├── node_modules
│   │  .
│   │  .
│   │  .
│   ├── package-lock.json
│   └── package.json
├── pod.yaml
├── service.yaml

## **2. Create files**
Create all the files needed for the lab:
Change directory to the certs folder
create the certificate with the below command:
Please note that we changed ip in the command to 10.151.130.198 that is the localhost of kike
run the below command to create the certificates:
```
openssl req -newkey rsa:2048 -nodes -keyout localhostkey.pem -x509 -days 365 -out localhostcert.pem -subj '/CN=test.keycloak.org/O=Test Keycloak./C=US' -extensions EXT -config <( \
echo -e "[dn]\nCN=test.keycloak.org\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:internal.oceloti.com,IP:10.151.130.198\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth")
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
NODE_API_PATH=node-api # Subdirectory for Node.js API

POD_FILES=pod.yaml service.yaml

# Build Keycloak Docker image
build-keycloak:
docker build -t $(DOCKER_IMAGE_KEYCLOAK) .

# Build Node.js Docker image
build-node:
docker build -t $(DOCKER_IMAGE_NODE) $(NODE_API_PATH)

# Apply Kubernetes configurations
up: build-keycloak build-node
kubectl create secret tls my-tls-secret --cert=certs/localhostcert.pem --key=.certs/localhostkey.pem
kubectl apply -f pod.yaml -f service.yaml 

# Apply Kubernetes configurations including Ingress
up-with-ingress: build-keycloak build-node
kubectl apply -f pod.yaml -f service.yaml -f ingress.yaml

# Delete Kubernetes resources
down:
kubectl delete secret my-tls-secret
kubectl delete -f pod.yaml -f service.yaml 

# Delete Kubernetes resources including Ingress
down-with-ingress:
kubectl delete -f pod.yaml -f service.yaml -f ingress.yaml

# Clean up dangling Docker images
clean:
docker system prune -f
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
---
apiVersion: v1
kind: Pod
metadata:
  name: node-pod
  labels:
    app: node-pod
spec:
  containers:
    - name: node-api
      image: node-api:latest
      imagePullPolicy: IfNotPresent # Use local image if available
      env:
        - name: KEYCLOAK_URL
          value: "https://keycloak-service:8443/auth" # Update URL to use HTTPS and match external port
      ports:
        - containerPort: 3000

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
      name: keycloak-https-port # Map Keycloak's HTTPS port
  type: NodePort
---
apiVersion: v1
kind: Service
metadata:
  name: node-service
spec:
  selector:
    app: node-pod
  ports:
    - protocol: TCP
      port: 3000
      targetPort: 3000
      nodePort: 32030
      name: nodejs-port
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
      name: postgres-port
  type: ClusterIP  
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
node-api/Dockerfile
Dockerfile (for node application):
```docker
# Use the latest Node.js image
FROM node:14

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to the container
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Expose the app's port
EXPOSE 3000

# Command to start the application
CMD ["npm", "start"]
```

index.js (Application):
```js
const express = require('express');
const app = express();
const port = 3000;

// Health check endpoint
app.get('/', (req, res) => {
    res.send('Hello from Node.js API!');
});

// Example secured endpoint
app.get('/secure', (req, res) => {
    res.send('This is a secured route!');
});

app.listen(port, () => {
    console.log(`Node.js API running at http://localhost:${port}`);
});
```

package.json
```json
{
  "name": "node-api",
  "version": "1.0.0",
  "description": "Node.js API for authentication lab",
  "main": "index.js",
  "scripts": {
    "start": "node index.js"
  },
  "dependencies": {
    "express": "^4.17.1"
  }
}
```

**You need to run the following command to have the setup correctly of the node app**
```bash
npm install
```

(!) If you see the node_modules then that means that it worked fine.

## **3. Install make in your WSL (ubuntu in windows in my case)**
Install make if not already installed (this will help to run the Makefile with the automation for starting the pod of kubernetes):
```bash
apt install make
```

## **4. Docker Desktop + Kubernetes**
Start Docker Desktop and start the Kubernetes service.
![[Pasted image 20241120151756.png]]

## **5. Run the Makefile**
For starting you need to use the following command:
```bash
make up
```

and for removing the pod you need the following:
```bash
make down
```

By the way the images will be still there so if you want you can delete them.

## **6. Check if worked right**

```bash
kubectl get pods
```
Result:
NAME           READY   STATUS    RESTARTS   AGE
keycloak-lab   3/3     Running   0          73m

```bash
kubectl get services
```
NAME                   TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)                                        AGE keycloak-lab-service   NodePort    10.105.173.5   none   5432:30432/TCP,8443:32080/TCP,3000:32030/TCP   74m kubernetes             ClusterIP   10.96.0.1      none        443/TCP                                        17h

Not used in this project!
```bash
kubectl get ingress
```


#### Extra
Command for entering in one machine like postgres database in our example:
```bash
kubectl exec -it keycloak-lab -c postgres -- bash
#this is to access a single pod:
kubectl exec -it keycloak-pod -- bash
#to see a secret:
kubectl get secrets
kubectl get secrets [secret-name] -o yaml
#to see the logs of a pod for troubleshooting
kubectl logs keycloak-pod
```

Check if the database is correct:
```bash
psql -U keycloak -d keycloak
```
query:
```sql
SELECT * FROM information_schema.tables;
```
for exiting use  \q

## **7. Android Implementation**
Look LabAuthentication inside documentation\CloudComputing\k8s-files\authentication_keycloak

# Final result:

Postgres machine:
![[Pasted image 20241120152417.png]]

Node API machine:
![[Pasted image 20241120152454.png]]

Keycloak machine (user: admin, password: admin):
![[Pasted image 20241120152535.png]]