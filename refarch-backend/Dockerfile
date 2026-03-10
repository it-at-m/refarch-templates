# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-2.1771324986@sha256:f2764fc96da28f9e1c08051b7fdf8f4282e76ba5b60637d6bbabf8a1ea45bf6e

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar