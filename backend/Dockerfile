# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-9@sha256:434a4e1c5d6d57c4380ddc0f2c1707ec65df1cb294ea84b21d37ca54aa4b923c

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar