variable "realm_id" {
  description = "Keycloak Realm ID"
  type        = string
}

variable "client_id" {
  description = "Client ID (OAuth2 client_id)"
  type        = string
}

variable "name" {
  description = "Display name of the client"
  type        = string
}

variable "description" {
  description = "Client description"
  type        = string
  default     = ""
}

variable "enabled" {
  description = "Enable client"
  type        = bool
  default     = true
}

variable "client_authenticator_type" {
  description = "Client authentication type (client-secret, client-jwt, etc.)"
  type        = string
  default     = "client-secret"
}

variable "standard_flow_enabled" {
  description = "Enable standard flow (Authorization Code)"
  type        = bool
  default     = true
}

variable "implicit_flow_enabled" {
  description = "Enable implicit flow"
  type        = bool
  default     = false
}

variable "direct_access_grants_enabled" {
  description = "Enable direct access grants (Resource Owner Password Credentials)"
  type        = bool
  default     = false
}

variable "service_accounts_enabled" {
  description = "Enable service accounts (Client Credentials)"
  type        = bool
  default     = false
}

variable "root_url" {
  description = "Root URL for the client"
  type        = string
  default     = ""
}

variable "valid_redirect_uris" {
  description = "List of valid redirect URIs"
  type        = list(string)
  default     = []
}

variable "web_origins" {
  description = "List of allowed CORS origins"
  type        = list(string)
  default     = ["+"]
}

variable "roles" {
  description = "Map of client roles with optional composite roles"
  type = map(object({
    description = optional(string, "")
    composite_roles = optional(list(object({
      role      = string
      client_id = optional(string, null) # null = realm role, else client role
    })), [])
  }))
  default = {}
}

variable "client_secret" {
  description = "Optional: Set a specific client secret (if not set, auto-generated)"
  type        = string
  sensitive   = true
  default     = null
}

variable "access_token_lifespan" {
  description = "Access token lifespan in seconds"
  type        = number
  default     = null
}

variable "default_client_scopes" {
  description = "List of default client scope names"
  type        = list(string)
  default     = ["profile", "email", "roles"]
}

variable "optional_client_scopes" {
  description = "List of optional client scope names"
  type        = list(string)
  default     = []
}

variable "admin_url" {
  description = "Admin URL for the client"
  type        = string
  default     = ""
}

variable "base_url" {
  description = "Base URL for the client"
  type        = string
  default     = ""
}

variable "full_scope_allowed" {
  description = "Allow full scope mapping"
  type        = bool
  default     = true
}

variable "consent_required" {
  description = "Require user consent"
  type        = bool
  default     = false
}

variable "frontchannel_logout" {
  description = "Enable frontchannel logout"
  type        = bool
  default     = false
}

variable "extra_attributes" {
  description = "Additional client attributes (e.g. backchannel.logout.session.required, oidc.ciba.grant.enabled, etc.)"
  type        = map(string)
  default     = {}
}

variable "authorization" {
  description = "Configuration for fine-grained authorization (enabled if present)"
  type = object({
    policy_enforcement_mode          = string
    decision_strategy                = optional(string)
    allow_remote_resource_management = optional(bool)
    keep_defaults                    = optional(bool)
  })
  default = null
}

# =============================================================================
# Protocol Mappers
# =============================================================================

variable "role_name_mappers" {
  description = "Role Name Mappers - Map client roles to different realm role names"
  type = map(object({
    role_name     = string  # Original role name (format: 'client_id.role_name' or 'role_name')
    new_role_name = string  # New name for the role
  }))
  default = {}
}

variable "user_attribute_mappers" {
  description = "User Attribute Mappers - Map user attributes to token claims"
  type = map(object({
    user_attribute      = string
    claim_name          = string
    claim_value_type    = optional(string, "String")
    add_to_id_token     = optional(bool, true)
    add_to_access_token = optional(bool, true)
    add_to_userinfo     = optional(bool, true)
  }))
  default = {}
}

variable "audience_mappers" {
  description = "Audience Mappers - Add audience to tokens"
  type = map(object({
    included_client_audience = string
    add_to_id_token          = optional(bool, true)
    add_to_access_token      = optional(bool, true)
  }))
  default = {}
}

variable "group_membership_mappers" {
  description = "Group Membership Mappers - Add group membership to tokens"
  type = map(object({
    claim_name          = string
    full_path           = optional(bool, false)
    add_to_id_token     = optional(bool, true)
    add_to_access_token = optional(bool, true)
    add_to_userinfo     = optional(bool, true)
  }))
  default = {}
}
