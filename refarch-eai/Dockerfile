# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.20-2.1729089285@sha256:fab89c4d8c652d881207027b27ee519e60fc210784160af66327065628030b5d

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar