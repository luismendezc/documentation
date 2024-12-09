# Define variables
DOCKER_IMAGE_KEYCLOAK=custom-keycloak:latest
DOCKER_IMAGE_JENKINS=custom-jenkins:latest
DOCKER_IMAGE_NODE=node-api:latest

POD_FILES=pod.yaml service.yaml

# Build Keycloak Docker image
build-keycloak:
	docker build -t $(DOCKER_IMAGE_KEYCLOAK) -f Dockerfile.keycloak .

# Build Jenkins Docker image
build-jenkins:
	docker build -t $(DOCKER_IMAGE_JENKINS) -f Dockerfile.jenkins .

# Build Node.js Docker image
build-node:
	docker build -t $(DOCKER_IMAGE_NODE) $(NODE_API_PATH)

# Apply Kubernetes configurations
up: build-keycloak	build-jenkins
	kubectl create secret tls my-tls-secret   --cert=certs/localhostcert.pem   --key=certs/localhostkey.pem
	kubectl apply -f pod.yaml -f service.yaml

# Delete Kubernetes resources
down:
	kubectl delete  -f pod.yaml -f service.yaml
	kubectl delete secret my-tls-secret
	kubectl delete secret keycloak-client-secret

# Clean up dangling Docker images
clean:
	docker system prune -f

# Populate the database
seed-db:
	kubectl exec -it postgres-pod -- psql -U keycloak -d keycloak -c "/scripts/seed.sql"


