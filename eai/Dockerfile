# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-4@sha256:3d41e8a91a8cf910bd9e5352826ed58b0e88992329d647a698b45a5f5d94929d

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar