# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.21-1.1737580425@sha256:06228e649f2d94b7d82de29af24c3764577e17e5cc31e48d963cc5f6b7fe2a08

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar