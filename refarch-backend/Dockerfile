# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-2.1774011801@sha256:622ba9ba5339b3af9bd4fd4bb9aad4d199ff3e2d0d1e50500d113aac38a801bf

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar