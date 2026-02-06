# Development Stack

See https://refarch.oss.muenchen.de/templates/develop.html#container-engine

## Keycloak Setup

The Keycloak is initialized via the `init-keycloak` service which uses OpenTofu and Terraform to configure the Keycloak 
with following Realms and Clients. The according Terraform files are stored inside `./keycloak/terraform`.

### local_realm

Default realm containing client `local`. 

By default, doesn't include any lhm-specific configuration but can be enabled by commenting in e.g. the `realm-scopes` module.

### LHM-Core

Realm containing lhm specific scopes (more see in [`realm-scopes` module docs](./keycloak/terraform/modules/realm-scopes/README.md)). Should only be used when needed to support maximum intercompatibility by reducing
dependencies on lhm particularities.
