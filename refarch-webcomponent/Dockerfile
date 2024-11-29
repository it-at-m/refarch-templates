# For documentation see https://github.com/sclorg/nginx-container
FROM registry.access.redhat.com/ubi9/nginx-124:9.5-1732617836@sha256:637dda3b651cc3e914cc98e7fce1c05ee310f0f90c7c9fec90886682484d86ca

# Copy built web components
COPY dist .

# Copy custom nginx configurations
COPY docker/nginx/*.conf "${NGINX_DEFAULT_CONF_PATH}"

# Start the web server
CMD nginx -g "daemon off;"