# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.21-1.1739376167@sha256:e8bc39a2a10ea39231b4c0d0b7b8728969169f78051d25d0a2e29311d0cf00f8

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar