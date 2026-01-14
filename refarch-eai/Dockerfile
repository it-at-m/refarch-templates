# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.23-6.1764764731@sha256:14e5af876865b25abdc15c137dd30e68b3394aaee18e26bd1777036a52a907c5

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar