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

