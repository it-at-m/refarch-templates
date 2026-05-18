# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-5@sha256:b4e6ba199191431a8a179b7ab18c41c3d201284de12d438452a30f534e85d144

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar