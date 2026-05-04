# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-3@sha256:52f952152d9d6ee352c529fc9df4a1ec90697b7ec5406f38458c76b66f526c6e

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar