# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.20-2.1726695169@sha256:4c90561565a199aeec97bad3628030c0deede8b797f3899c45c3761d3e242604

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar