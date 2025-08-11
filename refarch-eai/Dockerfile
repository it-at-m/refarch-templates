# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.22-1.1753981256@sha256:06d60e73be11d96b4cedc4bd1807e503a6196e9b01be9e4405b6b6d3b816202d

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar