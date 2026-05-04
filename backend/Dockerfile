# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-25-runtime:1.24-3@sha256:71780daf722e1401d997d262d90f976e4e37b4cd775f383ea024e975b5894b45

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar