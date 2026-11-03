# Note: For base images an architecture-agnostic SHA is always preferred, see https://refarch.oss.muenchen.de/templates/deploy.html#docker-images for more info.

# For documentation see https://rh-openjdk.github.io/redhat-openjdk-containers/
FROM registry.access.redhat.com/ubi10/openjdk-25-runtime:1.24-17@sha256:cfc8606329db9ee031c42c38ddbe11fe48980f3765822511a5bc6a321c73b193

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar