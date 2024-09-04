
# Copy Javascript-Code in einen Nginx Container
# Docs https://github.com/sclorg/nginx-container/blob/master/1.24/README.md
# All other variations must be approved by KM8
FROM registry.access.redhat.com/ubi9/nginx-124:latest
COPY dist/ /opt/app-root/src  

CMD nginx -g "daemon off;"