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
	kubectl create secret tls my-tls-secret   --cert=certs/localhostcert.pem   --key=.certs/localhostkey.pem
	kubectl apply -f pod.yaml -f service.yaml

# Apply Kubernetes configurations including Ingress
up-with-ingress: build-keycloak build-node
	kubectl apply -f pod.yaml -f service.yaml -f ingress.yaml

# Delete Kubernetes resources
down:
    kubectl delete secret my-tls-secret
	kubectl delete  -f pod.yaml -f service.yaml

# Delete Kubernetes resources including Ingress
down-with-ingress:
	kubectl delete -f pod.yaml -f service.yaml -f ingress.yaml

# Clean up dangling Docker images
clean:
	docker system prune -f