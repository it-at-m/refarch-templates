# ============================================================================
# LHM Demo Setup - SIMPLIFIED VERSION for fresh Keycloak instance
# ============================================================================

# 1. Create LHM-Demo realm
# ============================================================================
resource "keycloak_realm" "lhm_demo" {
  realm        = "LHM-Demo"
  enabled      = true
  display_name = "LHM Demo Realm"

  # LHM standard themes
  # login_theme = "lhm-default.v2"
  # admin_theme = "lhm-admin"

  # Token lifespans (LHM standard)
  sso_session_idle_timeout = "30m"
  sso_session_max_lifespan = "10h"
  access_token_lifespan    = "5m"

  # Internationalization
  internationalization {
    supported_locales = ["de", "en"]
    default_locale    = "de"
  }

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

# 1b. User profile with unmanaged attributes policy
# ============================================================================
resource "keycloak_realm_user_profile" "lhm_demo_profile" {
  realm_id = keycloak_realm.lhm_demo.id

  # 🎯 IMPORTANT: activate unmanaged attributes
  unmanaged_attribute_policy = "ENABLED"

  attribute {
    name         = "username"
    display_name = "Username"

    permissions {
      view = ["admin", "user"]
      edit = ["admin"]
    }

    validator {
      name = "length"
      config = {
        min = "3"
        max = "255"
      }
    }

    validator {
      name = "person-name-prohibited-characters"
    }

    validator {
      name = "up-username-not-idn-homograph"
    }
  }

  attribute {
    name         = "email"
    display_name = "Email"

    permissions {
      view = ["admin", "user"]
      edit = ["admin", "user"]
    }

    validator {
      name = "email"
    }

    validator {
      name = "length"
      config = {
        max = "255"
      }
    }
  }

  attribute {
    name         = "firstName"
    display_name = "First name"

    permissions {
      view = ["admin", "user"]
      edit = ["admin", "user"]
    }
  }

  attribute {
    name         = "lastName"
    display_name = "Last name"

    permissions {
      view = ["admin", "user"]
      edit = ["admin", "user"]
    }
  }
}

# 2. Create LHM standard scopes via module
# ============================================================================
module "lhm_demo_scopes" {
  source = "./modules/realm-scopes"

  # Realm config
  realm_id = keycloak_realm.lhm_demo.realm

  # NEW REALM MODE: Skip data source lookups during plan phase
  skip_default_scopes_lookup    = true
  manage_roles_scope            = false # roles scope is auto-created, managed separately
  use_custom_authorities_mapper = false

  depends_on = [keycloak_realm.lhm_demo]
}

# 2b. Realm optional client scopes - standard + custom LHM scopes
# ============================================================================
# Keycloak automatically creates standard scopes, but we need to explicitly add them
# to the optional scopes.
resource "keycloak_realm_optional_client_scopes" "lhm_demo_optional" {
  realm_id = keycloak_realm.lhm_demo.id

  optional_scopes = [
    # Standard Keycloak scopes (referenced by name)
    "profile",
    "email",
    "roles",
    "acr",
    "web-origins",
    "basic",
    # Custom LHM scopes
    "LHM",
    "LHM_Extended"
  ]

  depends_on = [
    module.lhm_demo_scopes
  ]
}

# 3. Clients via module
# ============================================================================
module "demo_confidential_client" {
  source = "./modules/oidc-client"

  realm_id    = keycloak_realm.lhm_demo.id
  client_id   = "lhm-demo-backend"
  name        = "LHM Demo Backend Application"
  description = "Confidential OIDC Client für Backend-Services"

  client_authenticator_type    = "client-secret"
  standard_flow_enabled        = true
  direct_access_grants_enabled = true
  service_accounts_enabled     = true
  full_scope_allowed           = false

  root_url            = "https://demo-backend.muenchen.de"
  valid_redirect_uris = ["https://demo-backend.muenchen.de/*", "http://localhost:8080/*"]
  web_origins         = ["https://demo-backend.muenchen.de", "http://localhost:8080"]

  # No scope assignment - Realm defaults are automatically applied
  # default_client_scopes  = ["profile", "email", "lhm-core"]
  # optional_client_scopes = ["roles"]

  roles = {
    "admin" = {
      description     = "Administrator role"
      composite_roles = []
    }
    "tester" = {
      description     = "Tester role"
      composite_roles = []
    }
    "viewer" = {
      description     = "Viewer role"
      composite_roles = []
    }
    "lhm-ab-demo-backend-admin" = {
      description = "⚠️ Internal AD role - grants admin access"
      composite_roles = [
        { role = "admin", client_id = "self" }
      ]
    }
    "lhm-ab-demo-backend-tester" = {
      description = "⚠️ Internal AD role - grants tester access"
      composite_roles = [
        { role = "tester", client_id = "self" }
      ]
    }
    "lhm-ab-demo-backend-viewer" = {
      description = "⚠️ Internal AD role - grants viewer access"
      composite_roles = [
        { role = "viewer", client_id = "self" }
      ]
    }
  }

  depends_on = [
    module.lhm_demo_scopes,
    keycloak_realm_optional_client_scopes.lhm_demo_optional
  ]
}

module "demo_public_client" {
  source = "./modules/oidc-client"

  realm_id    = keycloak_realm.lhm_demo.id
  client_id   = "lhm-demo-frontend"
  name        = "LHM Demo Frontend Application"
  description = "Public OIDC Client für SPAs"

  client_authenticator_type = "none" # Public client = no authentication
  standard_flow_enabled     = true
  full_scope_allowed        = false

  root_url            = "https://demo-frontend.muenchen.de"
  valid_redirect_uris = ["https://demo-frontend.muenchen.de/*", "http://localhost:3000/*", "http://localhost:5173/*"]
  web_origins         = ["https://demo-frontend.muenchen.de", "http://localhost:3000", "http://localhost:5173"]

  # No scope assignment - Realm defaults are automatically applied
  # default_client_scopes  = ["profile", "email", "lhm-core"]
  # optional_client_scopes = ["roles"]

  roles = {
    "admin" = {
      description     = "Administrator role"
      composite_roles = []
    }
    "tester" = {
      description     = "Tester role"
      composite_roles = []
    }
    "viewer" = {
      description     = "Viewer role"
      composite_roles = []
    }
    "lhm-ab-demo-frontend-admin" = {
      description = "⚠️ Internal AD role - grants admin access"
      composite_roles = [
        { role = "admin", client_id = "self" }
      ]
    }
    "lhm-ab-demo-frontend-tester" = {
      description = "⚠️ Internal AD role - grants tester access"
      composite_roles = [
        { role = "tester", client_id = "self" }
      ]
    }
    "lhm-ab-demo-frontend-viewer" = {
      description = "⚠️ Internal AD role - grants viewer access"
      composite_roles = [
        { role = "viewer", client_id = "self" }
      ]
    }
  }

  depends_on = [
    module.lhm_demo_scopes,
    keycloak_realm_optional_client_scopes.lhm_demo_optional
  ]
}

# 4. Demo users
# ============================================================================
module "demo_user_admin" {
  source = "./modules/keycloak-user"

  realm_id   = keycloak_realm.lhm_demo.id
  username   = "maria.admin"
  email      = "maria.admin@muenchen.de"
  first_name = "Maria"
  last_name  = "Admin"

  initial_password   = "Demo123!Admin"
  temporary_password = true

  department       = "IT-Referat - IAM"
  telephone_number = "+49 89 233-11111"
  member_of        = ["CN=lhm-ab-demo-backend-admin,OU=Groups,DC=muenchen,DC=de"]

  generate_lhm_object_id = true
  required_actions       = ["UPDATE_PASSWORD", "VERIFY_EMAIL"]

  # IMPORTANT: User profiles must be created BEFORE the users!
  depends_on = [keycloak_realm_user_profile.lhm_demo_profile]
}

module "demo_user_tester" {
  source = "./modules/keycloak-user"

  realm_id   = keycloak_realm.lhm_demo.id
  username   = "thomas.tester"
  email      = "thomas.tester@muenchen.de"
  first_name = "Thomas"
  last_name  = "Tester"

  initial_password   = "Demo123!Test"
  temporary_password = true

  department       = "IT-Referat - QA"
  telephone_number = "+49 89 233-22222"
  member_of        = ["CN=lhm-ab-demo-backend-tester,OU=Groups,DC=muenchen,DC=de"]

  generate_lhm_object_id = true
  required_actions       = ["UPDATE_PASSWORD", "VERIFY_EMAIL"]

  # IMPORTANT: User profiles must be created BEFORE the users!
  depends_on = [keycloak_realm_user_profile.lhm_demo_profile]
}

# Role assignments
resource "keycloak_user_roles" "admin_backend_roles" {
  realm_id = keycloak_realm.lhm_demo.id
  user_id  = module.demo_user_admin.user_id

  role_ids = [
    module.demo_confidential_client.client_roles["lhm-ab-demo-backend-admin"]
  ]
}

resource "keycloak_user_roles" "tester_backend_roles" {
  realm_id = keycloak_realm.lhm_demo.id
  user_id  = module.demo_user_tester.user_id

  role_ids = [
    module.demo_confidential_client.client_roles["lhm-ab-demo-backend-tester"]
  ]
}

# Outputs
output "lhm_demo_realm_info" {
  value = {
    realm_name   = keycloak_realm.lhm_demo.realm
    realm_id     = keycloak_realm.lhm_demo.id
    display_name = keycloak_realm.lhm_demo.display_name
    login_theme  = keycloak_realm.lhm_demo.login_theme
  }
}

output "lhm_demo_clients" {
  sensitive = true
  value = {
    backend = {
      client_id     = module.demo_confidential_client.client_id
      client_secret = module.demo_confidential_client.client_secret
    }
    frontend = {
      client_id = module.demo_public_client.client_id
    }
  }
}

output "lhm_demo_users" {
  sensitive = true
  value = {
    admin  = { username = "maria.admin", password = "Demo123!Admin" }
    tester = { username = "thomas.tester", password = "Demo123!Test" }
  }
}
