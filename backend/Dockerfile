# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi10/openjdk-21-runtime:1.24-10@sha256:3f21794f7e1c568a194798dc36f8bb09da239471fa88d7fa8353efaa091549cc

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar