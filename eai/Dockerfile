# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-2@sha256:e9f3f4b4dfe280b013b2d7ef17a3819136ffaa49f4310cdbc41d87c88de48a90

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar