# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-7@sha256:cbafedeac97c664c892ad4d776735662aa4cc3a4d5ab9f6c5a36d99e6f1f25fe

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar