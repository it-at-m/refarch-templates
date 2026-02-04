# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-2.1769595317@sha256:0cd7e2b5d871c4f549be47381c1b8c0c28a99c5425f61a2bbb9b4de9f2b155d1

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar