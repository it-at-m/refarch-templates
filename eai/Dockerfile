# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-6@sha256:b39bdcb3ed9c7bdbef51a8f2692f81a771303351a9d87e6887f8affe56bd197a

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar