# Note: For base images an architecture-agnostic SHA is always preferred, see https://refarch.oss.muenchen.de/templates/deploy.html#docker-images for more info.

# For documentation see https://rh-openjdk.github.io/redhat-openjdk-containers/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-11@sha256:2056c5139666c5e52f01af5b80f0def423a94c3fe824321d74041c11e20c9b72

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar