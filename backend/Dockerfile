# Note: For base images an architecture-agnostic SHA is always preferred, see https://refarch.oss.muenchen.de/templates/deploy.html#docker-images for more info.

# For documentation see https://rh-openjdk.github.io/redhat-openjdk-containers/
FROM registry.access.redhat.com/ubi10/openjdk-25-runtime:1.24-15@sha256:70b19e9729eb38dae0952370aea062c5a78c684b38485c52387b740a9486d4a1

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar