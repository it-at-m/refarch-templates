# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.20-2.1725851019@sha256:4ef4e9bdc908abca04b119002c6e00545101084d6e974d98c111bbf0b9885c13

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar