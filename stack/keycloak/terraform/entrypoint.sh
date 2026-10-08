#!/bin/sh
set -e



# Wait until Keycloak is available
echo "Waiting for Keycloak at ${KEYCLOAK_URL}..."
until wget --spider -q ${KEYCLOAK_URL}; do
  echo "Keycloak not ready yet. Sleeping 5s..."
  sleep 5
done

echo "Keycloak is up! Initializing Tofu..."

# Run Tofu commands
tofu fmt
tofu init
tofu apply -auto-approve

# Modify roles scope
./update_roles_scope.sh -k "$KEYCLOAK_URL" -u "$ADMIN_USER" -p "$ADMIN_PASSWORD" $KEYCLOAK_REALMS

echo "Finished"
