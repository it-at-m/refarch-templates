# For documentation see https://github.com/sclorg/nginx-container
FROM registry.access.redhat.com/ubi9/nginx-124:1-25.1729775649@sha256:c8902361a6d026ed7659b02725f0a744b13d3f443bcccf5108efbc49f4062988

# Copy built web components
COPY dist .

# Copy custom nginx configurations
COPY docker/nginx/*.conf "${NGINX_DEFAULT_CONF_PATH}"

# Start the web server
CMD nginx -g "daemon off;"