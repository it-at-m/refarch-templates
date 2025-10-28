# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.23-6.1761164966@sha256:e53c6cc7fe4e1cdf6baaea25ec472db62ac74f3f7cf41c12ce74f3407d81d872

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar