# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-2.1770236038@sha256:20252d0fd32221be2c6ffa91c19f0344c80c8ce3f054a26a2d7492a172516e53

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar