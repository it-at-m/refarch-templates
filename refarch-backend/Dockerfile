# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.22-1.1752676419@sha256:75fa8df8118eaa1710b5676192a10711768f82f02dd9303009aa2c8d23de5cd5

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar