terraform {
  required_providers {
    keycloak = {
      source  = "keycloak/keycloak"
      version = "~> 5.5.0"
    }
  }
}

# Filter realms based on blacklist
locals {
  enabled_realm_ids = [for r in var.realm_ids : r if !contains(var.realm_blacklist, r)]
  
  # Empty list if skipping lookups, otherwise enabled realms
  realms_for_lookup = var.skip_default_scopes_lookup ? [] : local.enabled_realm_ids
  
  # Protocol Mapper Overrides - Ermittelt realm-spezifische Attribute oder Default-Werte
  lhmObjectID_attributes = {
    for realm in local.enabled_realm_ids : realm => try(
      var.protocol_mapper_overrides[realm].lhmObjectID_user_attribute,
      "lhmObjectID"  # Default-Wert wenn realm nicht in overrides oder Attribut null
    )
  }
}

resource "keycloak_openid_client_scope" "common_client_scope" {
  for_each = toset(local.enabled_realm_ids)
  realm_id = each.key
  name     = "lhm-core"
  description = "LHM standard scope – Contains all user attributes typical for City of Munich applications."
  include_in_token_scope = true
}

# Roles Client Scope - Conditional: Resource oder Data Source
# The roles scope already exists in new realms, but must be created in old ones.
resource "keycloak_openid_client_scope" "roles_scope" {
  for_each = var.manage_roles_scope ? toset(local.enabled_realm_ids) : []
  
  realm_id = each.key
  name     = "roles"
  description = "OpenID Connect scope for add user roles to the access token"
  include_in_token_scope = true
  
  lifecycle {
    ignore_changes = [name]
  }
}

data "keycloak_openid_client_scope" "roles_scope_existing" {
  for_each = var.manage_roles_scope ? [] : toset(local.realms_for_lookup)
  
  realm_id = each.key
  name     = "roles"
}

# Unified local variable for roles scope ID
locals {
  roles_scope_ids = var.manage_roles_scope ? {
    for k, v in keycloak_openid_client_scope.roles_scope : k => v.id
  } : {
    for k, v in data.keycloak_openid_client_scope.roles_scope_existing : k => v.id
  }
}

# Data sources for built-in Keycloak scopes (with error handling)
data "keycloak_openid_client_scope" "profile" {
  for_each = toset(local.realms_for_lookup)
  realm_id = each.key
  name     = "profile"
  
  lifecycle {
    postcondition {
      condition     = self.id != ""
      error_message = "Client scope 'profile' not found in realm ${each.key}. Please create it manually or use --refresh."
    }
  }
}

data "keycloak_openid_client_scope" "email" {
  for_each = toset(local.realms_for_lookup)
  realm_id = each.key
  name     = "email"
  
  lifecycle {
    postcondition {
      condition     = self.id != ""
      error_message = "Client scope 'email' not found in realm ${each.key}. Please create it manually or use --refresh."
    }
  }
}

data "keycloak_openid_client_scope" "acr" {
  for_each = toset(local.realms_for_lookup)
  realm_id = each.key
  name     = "acr"
  
  lifecycle {
    postcondition {
      condition     = self.id != ""
      error_message = "Client scope 'acr' not found in realm ${each.key}. Please create it manually or use --refresh."
    }
  }
}

data "keycloak_openid_client_scope" "basic" {
  for_each = toset(local.realms_for_lookup)
  realm_id = each.key
  name     = "basic"
}

data "keycloak_openid_client_scope" "web_origins" {
  for_each = toset(local.realms_for_lookup)
  realm_id = each.key
  name     = "web-origins"
  
  lifecycle {
    postcondition {
      condition     = self.id != ""
      error_message = "Client scope 'web-origins' not found in realm ${each.key}. Please create it manually or use --refresh."
    }
  }
}

## lhm-core scope protocol mappers lhmObjectID
resource "keycloak_openid_user_attribute_protocol_mapper" "lhmObjectID" {
  for_each            = toset(var.realm_ids)
  realm_id            = each.key
  client_scope_id     = keycloak_openid_client_scope.common_client_scope[each.key].id
  name                = "lhmObjectID"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true

  # Realm-spezifisches user_attribute via protocol_mapper_overrides Variable
  user_attribute   = local.lhmObjectID_attributes[each.key]
  claim_value_type = "String"
  claim_name       = "lhmObjectID"
}

## lhm-core scope protocol mappers department
resource "keycloak_openid_user_attribute_protocol_mapper" "department" {
  for_each            = toset(var.realm_ids)
  realm_id            = each.key
  client_scope_id     = keycloak_openid_client_scope.common_client_scope[each.key].id
  name                = "department"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true

  user_attribute   = "department"
  claim_value_type = "String"
  claim_name       = "department"
}

## lhm-core scope protocol mappers telephoneNumber
# FIXME remove?
resource "keycloak_openid_user_attribute_protocol_mapper" "telephoneNumber" {
  for_each            = toset(var.realm_ids)
  realm_id            = each.key
  client_scope_id     = keycloak_openid_client_scope.common_client_scope[each.key].id
  name                = "telephoneNumber"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true

  user_attribute   = "telephoneNumber"
  claim_value_type = "String"
  claim_name       = "telephone_number"
}

# Default Scopes - only lhm-core
# resource "keycloak_realm_default_client_scopes" "default_scopes" {
#   for_each = toset(local.enabled_realm_ids)
#   realm_id = each.key
#
#   default_scopes = [
#     keycloak_openid_client_scope.common_client_scope[each.key].name
#   ]
# }

# Optional Scopes - Standard Keycloak Scopes + Custom LHM Scopes
# ONLY for existing realms (skip_default_scopes_lookup = false)
# For new realms, this is managed in the root module
resource "keycloak_realm_optional_client_scopes" "optional_scopes" {
  for_each        = var.skip_default_scopes_lookup ? [] : toset(local.enabled_realm_ids)
  realm_id        = each.key
  optional_scopes = concat(
    # Standard Keycloak Scopes - for existing realms
    [
      "profile",
      "email", 
      "acr",
      "web-origins",
      "basic",
      "roles"
    ],
    # Custom LHM Scopes
    var.optional_scopes
  )
}
