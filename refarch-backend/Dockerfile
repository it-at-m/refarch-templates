# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.21-1.1733995527@sha256:780c873d58f5b0acd82cb28ce273d529e73899c6150aa4366aee53aa251d495d

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar