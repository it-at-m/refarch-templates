# Dockerfiles may only contain a FROM and the application data.
# For Java applications use /ubi9/openjdk-11-runtime or /ubi9/openjdk-17-runtime as Base Image, for documentation 
# please see https://jboss-container-images.github.io/openjdk/
# All other variations must be approved by KM8

FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:latest

COPY target/*.jar /deployments/application.jar