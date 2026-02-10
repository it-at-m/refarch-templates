# create realm
resource "keycloak_realm" "local" {
  realm   = "local_realm"
  enabled = true
}

# scopes
# lhm-scopes (also comment in clients depends_on)
# module "scopes_local" {
#   source                        = "./modules/realm-scopes"
#   realm_id                      = keycloak_realm.local.id
#   skip_default_scopes_lookup    = true
#   manage_roles_scope            = false
#   use_custom_authorities_mapper = false
# }
# comment out with lhm scopes
resource "keycloak_realm_default_client_scopes" "local_default" {
  realm_id       = keycloak_realm.local.id
  default_scopes = []
}
resource "keycloak_realm_optional_client_scopes" "local_optional" {
  realm_id = keycloak_realm.local.id
  optional_scopes = [
    "profile",
    "email",
    "roles",
    "acr",
    "web-origins",
    "basic"
  ]
  depends_on = [
    keycloak_realm_default_client_scopes.local_default
  ]
}

# create client
module "client_local" {
  source              = "./modules/oidc-client"
  realm_id            = keycloak_realm.local.id
  client_id           = "local"
  client_secret       = "client_secret"
  name                = "local"
  valid_redirect_uris = ["http://*", "https://*"]
  roles = {
    "reader" = {
      description = "Example role with read access to application data"
    }
    "writer" = {
      description = "Example role with write access to application data"
    }
  }
  audience_mappers = {
    "audience-mapper" : {
      included_client_audience = "local"
    }
  }
  # permissions (see local-permissions.tf)
  # service_accounts_enabled = true
  # authorization = {
  #   policy_enforcement_mode = "ENFORCING"
  #   decision_strategy       = "AFFIRMATIVE"
  # }
  depends_on = [
    keycloak_realm_optional_client_scopes.local_optional,
    # module.scopes_local
  ]
}

# create users
module "user_none" {
  source                    = "./modules/keycloak-user"
  realm_id                  = keycloak_realm.local.id
  username                  = "none"
  first_name                = "none"
  last_name                 = "none"
  email                     = "none@example.com"
  email_verified            = true
  initial_password          = "none"
  temporary_password        = false
  custom_attributes_enabled = false
}

module "user_reader" {
  source                    = "./modules/keycloak-user"
  realm_id                  = keycloak_realm.local.id
  username                  = "reader"
  first_name                = "reader"
  last_name                 = "reader"
  email                     = "reader@example.com"
  email_verified            = true
  initial_password          = "reader"
  temporary_password        = false
  custom_attributes_enabled = false
}

module "user_writer" {
  source                    = "./modules/keycloak-user"
  realm_id                  = keycloak_realm.local.id
  username                  = "writer"
  first_name                = "writer"
  last_name                 = "writer"
  email                     = "writer@example.com"
  email_verified            = true
  initial_password          = "writer"
  temporary_password        = false
  custom_attributes_enabled = false
}

# assign roles
resource "keycloak_user_roles" "reader" {
  realm_id = keycloak_realm.local.id
  user_id  = module.user_reader.user_id
  role_ids = [
    module.client_local.client_roles["reader"]
  ]
}

resource "keycloak_user_roles" "writer" {
  realm_id = keycloak_realm.local.id
  user_id  = module.user_writer.user_id
  role_ids = [
    module.client_local.client_roles["writer"]
  ]
}
