terraform {
  required_providers {
    keycloak = {
      source  = "keycloak/keycloak"
      version = "~> 5.5.0"
    }
  }
}

resource "keycloak_openid_client_scope" "common_client_scope" {
  realm_id = var.realm_id
  name     = "lhm-core"
  description = "LHM standard scope – Contains all user attributes typical for City of Munich applications."
  include_in_token_scope = true
}

# Roles Client Scope - Conditional: Resource oder Data Source
# The roles scope already exists in new realms, but must be created in old ones.
resource "keycloak_openid_client_scope" "roles_scope" {
  count = var.manage_roles_scope ? 1 : 0
  realm_id = var.realm_id
  name     = "roles"
  description = "OpenID Connect scope for add user roles to the access token"
  include_in_token_scope = true
  
  lifecycle {
    ignore_changes = [name]
  }
}

data "keycloak_openid_client_scope" "roles_scope_existing" {
  count = var.manage_roles_scope ? 0 : 1
  realm_id = var.realm_id
  name     = "roles"
}

# Unified local variable for roles scope ID
locals {
  roles_scope_id = var.manage_roles_scope ? keycloak_openid_client_scope.roles_scope[0].id : data.keycloak_openid_client_scope.roles_scope_existing[0].id
}

# ⚠️ DEPRECATED: LHM Scope (Legacy - use lhm-core instead)
resource "keycloak_openid_client_scope" "lhm_scope" {
  realm_id               = var.realm_id
  name                   = "LHM"
  description            = "⚠️ DEPRECATED: Legacy scope for backward compatibility. Use 'lhm-core' for new applications. This scope will be removed in a future release."
  include_in_token_scope = true
}

resource "keycloak_openid_user_attribute_protocol_mapper" "lhm_telephone" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_scope.id
  name                = "telephoneNumber"
  user_attribute      = "telephoneNumber"
  claim_name          = "telephoneNumber"
  claim_value_type    = "String"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

resource "keycloak_openid_user_attribute_protocol_mapper" "lhm_surname" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_scope.id
  name                = "surname"
  user_attribute      = "lastName"
  claim_name          = "surname"
  claim_value_type    = "String"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

resource "keycloak_openid_user_property_protocol_mapper" "lhm_givenname" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_scope.id
  name                = "givenname"
  user_property       = "firstName"
  claim_name          = "givenname"
  claim_value_type    = "String"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

resource "keycloak_openid_user_attribute_protocol_mapper" "lhm_department" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_scope.id
  name                = "department"
  user_attribute      = "department"
  claim_name          = "department"
  claim_value_type    = "String"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

resource "keycloak_openid_user_attribute_protocol_mapper" "lhm_display_name" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_scope.id
  name                = "displayName"
  user_attribute      = "displayName"
  claim_name          = "displayName"
  claim_value_type    = "String"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

resource "keycloak_openid_user_attribute_protocol_mapper" "lhm_object_id" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_scope.id
  name                = "lhmObjectID"
  # Realm-spezifisches user_attribute via protocol_mapper_overrides Variable (auch für Legacy Scope)
  user_attribute      = var.lhmObjectID_attribute
  claim_name          = "lhmObjectID"
  claim_value_type    = "String"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

resource "keycloak_openid_user_property_protocol_mapper" "lhm_username" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_scope.id
  name                = "username"
  user_property       = "Username"
  claim_name          = "username"
  claim_value_type    = "String"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

resource "keycloak_openid_user_property_protocol_mapper" "lhm_email" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_scope.id
  name                = "email"
  user_property       = "email"
  claim_name          = "email"
  claim_value_type    = "String"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

# ⚠️ DEPRECATED: LHM_Extended Scope
resource "keycloak_openid_client_scope" "lhm_extended_scope" {
  realm_id               = var.realm_id
  name                   = "LHM_Extended"
  description            = "⚠️ DEPRECATED: Extended legacy scope for backward compatibility. Use 'lhm-core' for new applications. This scope will be removed in a future release."
  include_in_token_scope = true
}

# 1) dn (LDAP Entry DN) - environment-aware mapping
resource "keycloak_openid_user_attribute_protocol_mapper" "lhm_ext_dn" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_extended_scope.id
  name                = "dn"
  user_attribute      = var.attribute_mappings.ldap_entry_dn
  claim_name          = "dn"
  claim_value_type    = "String"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

# 2) User Authorities (Custom oder Realm Roles)
resource "keycloak_generic_protocol_mapper" "lhm_ext_authorities" {
  count           = var.use_custom_authorities_mapper ? 1 : 0
  realm_id        = var.realm_id
  client_scope_id = keycloak_openid_client_scope.lhm_extended_scope.id
  name            = "User Authorities mapper"
  protocol        = "openid-connect"
  protocol_mapper = "oidc-authorities-mapper"

  config = {
    "authorities"           = "authorities"
    "claim.name"            = "authorities"
    "id.token.claim"        = "true"
    "jsonType.label"        = "String"
    "multivalued"           = "true"
    "userinfo.token.claim"  = "true"
  }
}

resource "keycloak_openid_user_realm_role_protocol_mapper" "lhm_ext_authorities_fallback" {
  count = var.use_custom_authorities_mapper ? 0 : 1
  realm_id        = var.realm_id
  client_scope_id = keycloak_openid_client_scope.lhm_extended_scope.id
  name            = "User Authorities mapper"

  claim_name          = "authorities"
  claim_value_type    = "String"
  multivalued         = true
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
}

# 3) memberOf
resource "keycloak_openid_user_attribute_protocol_mapper" "lhm_ext_memberof" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_extended_scope.id
  name                = "memberof"
  user_attribute      = "memberof"
  claim_name          = "memberof"
  claim_value_type    = "String"
  multivalued         = true
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = false
}

# 4) ACR LoA Level
resource "keycloak_openid_hardcoded_claim_protocol_mapper" "lhm_ext_acr_loa" {
  realm_id         = var.realm_id
  client_scope_id  = keycloak_openid_client_scope.lhm_extended_scope.id
  name             = "acr loa level"
  claim_name       = "acr"
  claim_value      = "loa-1"
  claim_value_type = "String"
  add_to_id_token       = true
  add_to_userinfo       = false
  add_to_access_token   = false
}

# 5) Audience Resolve
resource "keycloak_openid_audience_resolve_protocol_mapper" "lhm_ext_audience" {
  realm_id        = var.realm_id
  client_scope_id = keycloak_openid_client_scope.lhm_extended_scope.id
  name            = "audience resolve"
}

# 6) Client Roles
resource "keycloak_openid_user_client_role_protocol_mapper" "lhm_ext_client_roles" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.lhm_extended_scope.id
  name                = "client roles"
  claim_name          = "resource_access.$${client_id}.roles"
  claim_value_type    = "String"
  multivalued         = true
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true
  
  lifecycle {
    ignore_changes = [claim_name]
  }
}

# Client Roles Mapper für den roles Scope
# Nur erstellen wenn wir den roles Scope selbst managen
# In existierenden scopes ist der Mapper bereits vorhanden
resource "keycloak_openid_user_client_role_protocol_mapper" "client_roles_mapper" {
  count            = var.manage_roles_scope ? 1 : 0
  realm_id        = var.realm_id
  client_scope_id = local.roles_scope_id

  name                = "client roles"
  // Standard: nur im Access-Token, jetzt auch im UserInfo
  add_to_access_token = true
  add_to_userinfo     = true
  add_to_id_token     = true

  multivalued         = true

  // In vielen Keycloak-Defaults wird der Claim-Name dynamisch mit ${client_id} gesetzt:
  // passe das bei Bedarf an den Ist-Zustand an, damit Plan/Drift minimal ist
  claim_name          = "resource_access.$${client_id}.roles"

  // Hinweis: Falls der Provider hier ein client_id-Attribut verlangt und der Default-Mapper
  // dynamisch ist, lasse es leer oder entferne die Zeile. Nur setzen, wenn du statisch
  // die Rollen EINES Clients mappen willst.
  // client_id        = ""
  
  lifecycle {
    // Verhindert unnötige Drifts, falls der bestehende Mapper minimal andere Defaults hat
    ignore_changes = [
      claim_name,
      // client_id, // nur ignorieren, wenn du client_id überhaupt setzt
    ]
  }
}

## lhm-core scope protocol mappers lhmObjectID
resource "keycloak_openid_user_attribute_protocol_mapper" "lhmObjectID" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.common_client_scope.id
  name                = "lhmObjectID"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true

  # Realm-spezifisches user_attribute via protocol_mapper_overrides Variable
  user_attribute   = var.lhmObjectID_attribute
  claim_value_type = "String"
  claim_name       = "lhmObjectID"
}

## lhm-core scope protocol mappers department
resource "keycloak_openid_user_attribute_protocol_mapper" "department" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.common_client_scope.id
  name                = "department"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true

  user_attribute   = "department"
  claim_value_type = "String"
  claim_name       = "department"
}

## lhm-core scope protocol mappers telephoneNumber
resource "keycloak_openid_user_attribute_protocol_mapper" "telephoneNumber" {
  realm_id            = var.realm_id
  client_scope_id     = keycloak_openid_client_scope.common_client_scope.id
  name                = "telephoneNumber"
  add_to_id_token     = true
  add_to_userinfo     = true
  add_to_access_token = true

  user_attribute   = "telephoneNumber"
  claim_value_type = "String"
  claim_name       = "telephone_number"
}

resource "keycloak_realm_default_client_scopes" "default_scopes" {
  realm_id = var.realm_id
  
  default_scopes = [
    keycloak_openid_client_scope.common_client_scope.name
  ]
}
# Default Scopes - only lhm-core

# Optional Scopes - Standard Keycloak Scopes + Custom LHM Scopes
# ONLY for existing realms (skip_default_scopes_lookup = false)
# For new realms, this is managed in the root module
resource "keycloak_realm_optional_client_scopes" "optional_scopes" {
  count           = var.skip_default_scopes_lookup ? 0 : 1
  realm_id        = var.realm_id
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
