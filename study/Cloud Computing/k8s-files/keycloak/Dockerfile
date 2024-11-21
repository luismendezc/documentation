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