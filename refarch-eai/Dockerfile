# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.22-1.1743605859@sha256:919e0f42342b48b692543bbc62ee7e8beeebc8f00f36b768d47b1542ba619ab0

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar