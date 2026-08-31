# Note: For base images an architecture-agnostic SHA is always preferred, see https://refarch.oss.muenchen.de/templates/deploy.html#docker-images for more info.

# For documentation see https://rh-openjdk.github.io/redhat-openjdk-containers/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-13@sha256:07122e5a2806f4d570fe3dfadeacd2d55b440b05f23e4639d1ae4966941431bb

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar