# For documentation see https://github.com/sclorg/nginx-container
FROM registry.access.redhat.com/ubi9/nginx-124:9.5-1739782542@sha256:e73b92e682108cafd247be56a64e8b5f0c906c6033c9740325cf8ff7c47320c9

# Copy built web application
COPY dist .

# Copy custom nginx configurations
COPY docker/nginx/*.conf "${NGINX_DEFAULT_CONF_PATH}"

# Start the web server
CMD nginx -g "daemon off;"