# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.23-6.1763034977@sha256:7ca4874ffc223925d225d636bac6fa302a09a4f3534ab4e072a6b75ee28d5415

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar