FROM ghcr.io/opentofu/opentofu:1-minimal AS tofu

FROM alpine:3.20

# Copy the tofu binary from the minimal image
COPY --from=tofu /usr/local/bin/tofu /usr/local/bin/tofu

# Add curl and jq
RUN apk add --no-cache bash curl jq

# Your application setup
WORKDIR /workspace
