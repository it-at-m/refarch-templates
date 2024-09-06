# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.20-2.1724037311@sha256:4536f1460fd9c75c300b7d0785a282c282d595d1d35f68ba10508b929dd74261

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar