# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-2@sha256:eac9bb8ff9ef9f1e69925011e211c4d33bdca9fd4925dddfafb0bb0d451a3ad0

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar