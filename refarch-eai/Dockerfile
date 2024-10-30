# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.20-2.1729773452@sha256:85e4c2b0768a4fc16ee577850fc4d7609b648aa72a98a2a1ddc0ac79b886b55e

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar