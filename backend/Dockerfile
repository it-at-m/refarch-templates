# For documentation see https://jboss-container-images.github.io/openjdk/
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24-2.1776357028@sha256:4f77a4aef51bb959a7af413b702d2b5db9b530c9bc09d10758947d5c73b7bafc

# Copy runnable jar to deployments
COPY target/*.jar /deployments/application.jar