# Load session lifespans
module "lifetimes" {
  source   = "github.com/it-at-m/keycloak-terraform//modules/realm-session-lifetimes?ref=init"
  realm_id = ["local_realm"]
}

# Create realm
resource "keycloak_realm" "local" {
  realm   = "local_realm"
  enabled = true

  # LHM session lifetimes
  # sso_session_idle_timeout             = module.lifetimes.settings["local_realm"].sso_session_idle_timeout
  # sso_session_max_lifespan             = module.lifetimes.settings["local_realm"].sso_session_max_lifespan
  # revoke_refresh_token                 = module.lifetimes.settings["local_realm"].revoke_refresh_token
  # refresh_token_max_reuse              = module.lifetimes.settings["local_realm"].refresh_token_max_reuse
  # access_token_lifespan                = module.lifetimes.settings["local_realm"].access_token_lifespan
  # offline_session_idle_timeout         = module.lifetimes.settings["local_realm"].offline_session_idle_timeout
  # offline_session_max_lifespan_enabled = module.lifetimes.settings["local_realm"].offline_session_max_lifespan_enabled
  # offline_session_max_lifespan         = module.lifetimes.settings["local_realm"].offline_session_max_lifespan
  # access_code_lifespan_login           = module.lifetimes.settings["local_realm"].access_code_lifespan_login
  # access_code_lifespan_user_action     = module.lifetimes.settings["local_realm"].access_code_lifespan_user_action

  # Security defenses
  security_defenses {
    headers {
      x_frame_options                     = "SAMEORIGIN"
      content_security_policy             = "frame-src 'self'; frame-ancestors 'self'; object-src 'none';"
      content_security_policy_report_only = ""
      x_content_type_options              = "nosniff"
      x_robots_tag                        = "none"
      x_xss_protection                    = "1; mode=block"
      strict_transport_security           = "max-age=31536000; includeSubDomains"
    }
    brute_force_detection {
      permanent_lockout                = false
      max_login_failures               = 5
      wait_increment_seconds           = 60
      quick_login_check_milli_seconds  = 1000
      minimum_quick_login_wait_seconds = 60
      max_failure_wait_seconds         = 900
      failure_reset_time_seconds       = 43200
    }
  }
}

# Create LHM scopes
module "lhm-scopes" {
  source = "github.com/it-at-m/keycloak-terraform//modules/realm-scopes?ref=init"

  realm_id                      = [keycloak_realm.local.realm]
  skip_default_scopes_lookup    = true
  manage_roles_scope            = false # roles scope is auto-created, managed separately
  use_custom_authorities_mapper = false
}
resource "keycloak_realm_default_client_scopes" "local_default" {
  realm_id       = keycloak_realm.local.id
  default_scopes = []
}
resource "keycloak_realm_optional_client_scopes" "local" {
  realm_id = keycloak_realm.local.id

  optional_scopes = [
    # Built-in keycloak scopes
    "profile",
    "email",
    "roles",
    "acr",
    "web-origins",
    "basic",
    # Custom LHM scopes
    # "lhm-core",
    # "LHM",
    # "LHM_Extended"
  ]

  depends_on = [
    keycloak_realm_optional_client_scopes.local,
    module.lhm-scopes
  ]
}

# Create local client
module "client-local" {
  source = "github.com/it-at-m/keycloak-terraform//modules/oidc-client?ref=init"

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

  depends_on = [
    keycloak_realm_optional_client_scopes.local,
    module.lhm-scopes
  ]
}

# Create users
module "user_none" {
  source                    = "github.com/it-at-m/keycloak-terraform//modules/keycloak-user?ref=init"
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
  source                    = "github.com/it-at-m/keycloak-terraform//modules/keycloak-user?ref=init"
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
  source                    = "github.com/it-at-m/keycloak-terraform//modules/keycloak-user?ref=init"
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

# Assign roles
resource "keycloak_user_roles" "reader" {
  realm_id = keycloak_realm.local.id
  user_id  = module.user_reader.user_id
  role_ids = [
    module.client-local.client_roles["reader"]
  ]
}

resource "keycloak_user_roles" "writer" {
  realm_id = keycloak_realm.local.id
  user_id  = module.user_writer.user_id
  role_ids = [
    module.client-local.client_roles["writer"]
  ]
}
