# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.23-6.1755674729@sha256:c6920b474dc2c79b53e901383c7a1274433bb84e83ecf73ad3284bd2ec1c2d84

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar