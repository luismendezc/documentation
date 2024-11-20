
## **1. Project structure**
Create directory where the files will be located:
/labAuthentication

**Structure of the project:**
.
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
        kubectl apply -f pod.yaml -f service.yaml

# Apply Kubernetes configurations including Ingress
up-with-ingress: build-keycloak build-node
        kubectl apply -f pod.yaml -f service.yaml -f ingress.yaml

# Delete Kubernetes resources
down:
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

# Enable health and metrics
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Configure database vendor
ENV KC_DB=postgres

WORKDIR /opt/keycloak

# Generate a demo key store (not for production)
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 \
    -dname "CN=server" -alias server \
    -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore

# Optimize the Keycloak build
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:latest
COPY --from=builder /opt/keycloak/ /opt/keycloak/

# Set environment variables for PostgreSQL and Keycloak
ENV KC_DB=postgres
ENV KC_DB_URL=jdbc:postgresql://localhost:5432/keycloak
ENV KC_DB_USERNAME=keycloak
ENV KC_DB_PASSWORD=mypassword
ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin
ENV KC_BOOTSTRAP_ADMIN_PASSWORD=admin
ENV KC_HOSTNAME=http://localhost:32080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
CMD ["start", "--optimized"]
```

**File including the pod definition:**
**pod.yaml**
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: keycloak-lab
  labels:
    app: keycloak-lab
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

    - name: keycloak
      image: custom-keycloak:latest
      imagePullPolicy: IfNotPresent # Use local image if available
      env:
        - name: KC_DB
          value: "postgres"
        - name: KC_DB_URL
          value: "jdbc:postgresql://localhost:5432/keycloak"
        - name: KC_DB_USERNAME
          value: "keycloak"
        - name: KC_DB_PASSWORD
          value: "mypassword"
        - name: KC_BOOTSTRAP_ADMIN_USERNAME
          value: "admin"
        - name: KC_BOOTSTRAP_ADMIN_PASSWORD
          value: "admin"
        - name: KC_HOSTNAME
          value: "localhost"
        - name: KC_HTTPS_PORT
          value: "8443" # Explicitly configure HTTPS port
      ports:
        - containerPort: 8443

    - name: node-api
      image: node-api:latest
      imagePullPolicy: IfNotPresent # Use local image if available
      env:
        - name: KEYCLOAK_URL
          value: "https://localhost:32080/auth" # Update URL to use HTTPS and match external port
      ports:
        - containerPort: 3000

```
Service to configure the ports:
service.yaml
```yaml
apiVersion: v1
kind: Service
metadata:
  name: keycloak-lab-service
spec:
  selector:
    app: keycloak-lab
  ports:
    - protocol: TCP
      port: 5432
      targetPort: 5432
      nodePort: 30432
      name: postgres-port
    - protocol: TCP
      port: 8443
      targetPort: 8443
      nodePort: 32080
      name: keycloak-https-port # Map Keycloak's HTTPS port
    - protocol: TCP
      port: 3000
      targetPort: 3000
      nodePort: 32030
      name: nodejs-port
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


# Final result:

Postgres machine:
![[Pasted image 20241120152417.png]]

Node API machine:
![[Pasted image 20241120152454.png]]

Keycloak machine (user: admin, password: admin):
![[Pasted image 20241120152535.png]]