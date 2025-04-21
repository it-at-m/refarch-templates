# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.22-1.1744796716@sha256:b3986db15797dd3ad4cb754e1bf0d75cd346ac63ef5843d810cd75b43d820826

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar