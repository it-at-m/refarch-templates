# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-2.1775561370@sha256:19dd5df9328c3c9b0d26b97a173a3d4ce12850699df148df2d86da2060ae403f

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar