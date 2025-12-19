#!/bin/sh
set -e

set -e

# Wait until Keycloak is available
echo "Waiting for Keycloak at http://keycloak:8100..."
until wget --spider -q http://keycloak:8100; do
  echo "Keycloak not ready yet. Sleeping 5s..."
  sleep 5
done

echo "Keycloak is up! Initializing Tofu..."

# Run Tofu commands
tofu fmt
tofu init
tofu apply -auto-approve

echo "Finished"
