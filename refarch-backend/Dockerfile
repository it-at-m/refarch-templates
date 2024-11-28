# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.21-1@sha256:77ddb49be45d284b0996f669f1465b4d8b0d0cc2147e79d150f70bc68143940c

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar