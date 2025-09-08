# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.23-6.1756793420@sha256:381e4f75c654b4e60b6a39147e803480f85bd3af7db291c0c522811c94f31848

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar