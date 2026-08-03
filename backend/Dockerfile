# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-11@sha256:0adb5132beb506b62fc15d86619a761868003506e0e2b5b6ca9ba82be519ca64

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar