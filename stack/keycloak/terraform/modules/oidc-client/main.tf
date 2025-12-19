terraform {
  required_providers {
    keycloak = {
      source  = "keycloak/keycloak"
      version = "~> 5.5.0"
    }
  }
}

resource "keycloak_openid_client" "client" {
  realm_id  = var.realm_id
  client_id = var.client_id
  name      = var.name
  enabled   = var.enabled

  description = var.description

  # Client Authentication: CONFIDENTIAL if client-secret/client-jwt, else PUBLIC
  client_authenticator_type = var.client_authenticator_type
  access_type               = var.client_authenticator_type == "client-secret" || var.client_authenticator_type == "client-jwt" || var.client_authenticator_type == "client-x509" ? "CONFIDENTIAL" : "PUBLIC"

  # Flows
  standard_flow_enabled        = var.standard_flow_enabled
  implicit_flow_enabled        = var.implicit_flow_enabled
  direct_access_grants_enabled = var.direct_access_grants_enabled
  service_accounts_enabled     = var.service_accounts_enabled

  # URLs
  root_url            = var.root_url
  admin_url           = var.admin_url
  base_url            = var.base_url
  valid_redirect_uris = var.valid_redirect_uris
  web_origins         = var.web_origins

  # Consent & Scope
  consent_required   = var.consent_required
  full_scope_allowed = var.full_scope_allowed

  # Optional: Custom client secret
  client_secret = var.client_secret

  # Extra config for advanced settings
  extra_config = merge(
    var.access_token_lifespan != null ? {
      "access.token.lifespan" = tostring(var.access_token_lifespan)
    } : {},
    var.frontchannel_logout ? {
      "frontchannel.logout.url" = ""
    } : {},
    var.extra_attributes
  )
}

# =============================================================================
# Client Scopes Assignment
# =============================================================================

# Lookup Default Client Scopes
data "keycloak_openid_client_scope" "default_scopes" {
  for_each = toset(var.default_client_scopes)
  
  realm_id = var.realm_id
  name     = each.value
}

# Lookup Optional Client Scopes
data "keycloak_openid_client_scope" "optional_scopes" {
  for_each = toset(var.optional_client_scopes)
  
  realm_id = var.realm_id
  name     = each.value
}

# Assign Default Client Scopes to Client
# IMPORTANT: This resource ONLY manages the explicitly specified scopes and REMOVES all other scopes from the client!
resource "keycloak_openid_client_default_scopes" "default_scopes" {
  count = length(var.default_client_scopes) > 0 ? 1 : 0

  realm_id  = var.realm_id
  client_id = keycloak_openid_client.client.id

  default_scopes = [
    for scope in var.default_client_scopes :
    data.keycloak_openid_client_scope.default_scopes[scope].id
  ]
}

# Assign Optional Client Scopes to Client  
resource "keycloak_openid_client_optional_scopes" "optional_scopes" {
  count = length(var.optional_client_scopes) > 0 ? 1 : 0

  realm_id  = var.realm_id
  client_id = keycloak_openid_client.client.id

  optional_scopes = [
    for scope in var.optional_client_scopes :
    data.keycloak_openid_client_scope.optional_scopes[scope].id
  ]
}

# Prepare composite assignment
locals {
  composite_mappings = flatten([
    for role_name, role_def in var.roles : [
      for composite in try(role_def.composite_roles, []) : {
        parent_role = role_name
        child_role  = composite.role
        client_id   = try(composite.client_id, null) // null = Realm-Role, "self" = gleicher Client, sonst Client-UUID
      }
    ]
  ])
}

# Realm roles (client_id = null)
data "keycloak_role" "realm_composite_roles" {
  for_each = {
    for m in local.composite_mappings :
    "${m.parent_role}:${m.child_role}" => m
    if m.client_id == null
  }
  realm_id = var.realm_id
  name     = each.value.child_role
}

# Foreign client roles (client_id != null && != “self”) – expects client UUID
data "keycloak_role" "client_composite_roles" {
  for_each = {
    for m in local.composite_mappings :
    "${m.parent_role}:${m.child_role}:${m.client_id}" => m
    if m.client_id != null && m.client_id != "self"
  }
  realm_id  = var.realm_id
  client_id = each.value.client_id
  name      = each.value.child_role
}

# 1. Create basic roles (without composites)
resource "keycloak_role" "base_roles" {
  for_each = {
    for role_name, role_def in var.roles :
    role_name => role_def
    if length(try(role_def.composite_roles, [])) == 0
  }

  realm_id    = var.realm_id
  client_id   = keycloak_openid_client.client.id
  name        = each.key
  description = try(each.value.description, "")
}

# 2. Composite roles separately (can refer to base_roles)
resource "keycloak_role" "composite_roles" {
  for_each = {
    for role_name, role_def in var.roles :
    role_name => role_def
    if length(try(role_def.composite_roles, [])) > 0
  }

  realm_id    = var.realm_id
  client_id   = keycloak_openid_client.client.id
  name        = each.key
  description = try(each.value.description, "")

  composite_roles = concat(
    # Realm roles
    [
      for m in local.composite_mappings :
      data.keycloak_role.realm_composite_roles["${m.parent_role}:${m.child_role}"].id
      if m.parent_role == each.key && m.client_id == null
    ],
    # Basis roles same Clients (client_id = "self")
    [
      for m in local.composite_mappings :
      keycloak_role.base_roles[m.child_role].id
      if m.parent_role == each.key && m.client_id == "self"
    ],
    # Foreign client roles
    [
      for m in local.composite_mappings :
      data.keycloak_role.client_composite_roles["${m.parent_role}:${m.child_role}:${m.client_id}"].id
      if m.parent_role == each.key && m.client_id != null && m.client_id != "self"
    ]
  )

  depends_on = [keycloak_role.base_roles]
}

# 3. Output for all roles (merge of both resources)
locals {
  all_roles = merge(
    { for k, v in keycloak_role.base_roles : k => v.id },
    { for k, v in keycloak_role.composite_roles : k => v.id }
  )
}

# =============================================================================
# Protocol Mappers
# =============================================================================

# Role Name Mappers - Map client roles to different realm role names
# Using generic protocol mapper since keycloak_openid_role_name_mapper doesn't exist
resource "keycloak_generic_protocol_mapper" "role_name_mappers" {
  for_each = var.role_name_mappers

  realm_id        = var.realm_id
  client_id       = keycloak_openid_client.client.id
  name            = each.key
  protocol        = "openid-connect"
  protocol_mapper = "oidc-role-name-mapper"

  config = {
    "role"          = each.value.role_name
    "new.role.name" = each.value.new_role_name
  }
}

# User Attribute Mappers
resource "keycloak_openid_user_attribute_protocol_mapper" "user_attribute_mappers" {
  for_each = var.user_attribute_mappers

  realm_id  = var.realm_id
  client_id = keycloak_openid_client.client.id
  name      = each.key

  user_attribute   = each.value.user_attribute
  claim_name       = each.value.claim_name
  claim_value_type = try(each.value.claim_value_type, "String")
  
  add_to_id_token     = try(each.value.add_to_id_token, true)
  add_to_access_token = try(each.value.add_to_access_token, true)
  add_to_userinfo     = try(each.value.add_to_userinfo, true)
}

# Audience Mappers
resource "keycloak_openid_audience_protocol_mapper" "audience_mappers" {
  for_each = var.audience_mappers

  realm_id  = var.realm_id
  client_id = keycloak_openid_client.client.id
  name      = each.key

  included_client_audience = each.value.included_client_audience
  
  add_to_id_token     = try(each.value.add_to_id_token, true)
  add_to_access_token = try(each.value.add_to_access_token, true)
}

# Group Membership Mappers
resource "keycloak_openid_group_membership_protocol_mapper" "group_mappers" {
  for_each = var.group_membership_mappers

  realm_id  = var.realm_id
  client_id = keycloak_openid_client.client.id
  name      = each.key

  claim_name = each.value.claim_name
  full_path  = try(each.value.full_path, false)
  
  add_to_id_token     = try(each.value.add_to_id_token, true)
  add_to_access_token = try(each.value.add_to_access_token, true)
  add_to_userinfo     = try(each.value.add_to_userinfo, true)
}
