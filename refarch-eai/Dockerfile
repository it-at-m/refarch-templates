# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.21-1.1739345077@sha256:81d3b2c8eb3f15749eb156fe12419fed74d0cc8f00bbc1721371d42650fc112d

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar