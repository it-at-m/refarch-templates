# For documentation see https://github.com/sclorg/nginx-container
FROM registry.access.redhat.com/ubi9/nginx-124:9.5-1737483922@sha256:a39459f55e5f8df68b9b58fd33cd53c8acabbbb9cd3df1a071187e071858f32f

# Copy built web components
COPY dist .

# Copy custom nginx configurations
COPY docker/nginx/*.conf "${NGINX_DEFAULT_CONF_PATH}"

# Start the web server
CMD nginx -g "daemon off;"