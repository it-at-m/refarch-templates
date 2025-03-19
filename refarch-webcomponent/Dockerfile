# For documentation see https://github.com/sclorg/nginx-container
FROM registry.access.redhat.com/ubi9/nginx-124:9.5-1741883852@sha256:4dd6bce207c829ee5e66b8e02218ce8d315345893611b1241ead6cb0f36fa89e

# Copy built web components
COPY dist .

# Copy custom nginx configurations
COPY docker/nginx/*.conf "${NGINX_DEFAULT_CONF_PATH}"

# Start the web server
CMD nginx -g "daemon off;"