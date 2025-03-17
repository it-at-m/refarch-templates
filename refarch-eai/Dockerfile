# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.21-1.1741781258@sha256:360822c35c5741f542ab78fe123e6c4d9b68e0113a88d6e0250bb1f377b17f29

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar