# For documentation see https://github.com/sclorg/nginx-container
FROM registry.access.redhat.com/ubi10/nginx-126:10.0-1758744794@sha256:0df9e18b00efd7e230c021e4511bab9904f59615d83421c178d611835a49f634

# Copy built web application
COPY dist .

# Copy custom nginx configurations
COPY docker/nginx/*.conf "${NGINX_DEFAULT_CONF_PATH}"

# Start the web server
CMD ["nginx", "-g", "daemon off;"]