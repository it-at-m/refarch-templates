FROM ghcr.io/opentofu/opentofu:1.9.1-minimal AS tofu

FROM alpine:3.24.2

# Copy the tofu binary from the minimal image
COPY --from=tofu /usr/local/bin/tofu /usr/local/bin/tofu

# Add curl and jq
RUN apk add --no-cache bash curl jq git

# Copy scripts
COPY entrypoint.sh /bin
COPY update_roles_scope.sh /bin
