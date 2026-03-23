# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-2.1773186561@sha256:5a47be89b07c96ad19d766ff7266c289fe38fc2c4769c0bb7f2e67b782c8f18e

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar