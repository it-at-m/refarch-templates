# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.23-6.1762870925@sha256:17009c3d3b149bb98097b65fdf43c8b7c37293ae280ee503fbae72103e9ac434

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar