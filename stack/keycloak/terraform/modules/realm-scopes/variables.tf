variable "client_id" {
  description = "Client ID"
  type        = string
  default     = "terraform"
}

variable "client_secret" {
  description = "Client Secret"
  type        = string
}

variable "keycloak_url" {
  description = "Keycloak URL"
  type        = string
}

variable "realm" {
  description = "Realm"
  type        = string
  default     = "master"
}

variable "optional_scopes" {
    description = "List of optional client scopes (custom scopes only - 'roles' is added automatically based on skip_default_scopes_lookup)"
    type        = list(string)
    default     = [
        "LHM",
        "LHM_Extended"
    ]
}


variable "realm_id" {
  description = "Realm IDs"
  type        = list(string)
  default     = []
}

variable "realm_blacklist" {
  description = "Realms to exclude from scope management"
  type        = list(string)
  default     = []
}

# Environment-specific attribute mappings for environments without LDAP
variable "attribute_mappings" {
  description = "Override attribute mappings for environments without LDAP (e.g., local, dev)"
  type = object({
    ldap_entry_dn = optional(string, "LDAP_ENTRY_DN")
    # Weitere Attribute können hier hinzugefügt werden
  })
  default = {
    ldap_entry_dn = "LDAP_ENTRY_DN"
  }
}

variable "manage_roles_scope" {
  description = "Whether to manage the roles scope as a resource (false = use data source for existing scope)"
  type        = bool
  default     = true
}

variable "skip_default_scopes_lookup" {
  description = "Skip data source lookups for default Keycloak scopes (set to true for NEW realm deployments to avoid 404 errors during plan phase)"
  type        = bool
  default     = false
}

# Realm-spezifische Protocol Mapper Overrides
variable "protocol_mapper_overrides" {
  description = "Realm-specific overrides for protocol mapper attributes (e.g., different user_attribute per realm)"
  type = map(object({
    lhmObjectID_user_attribute = optional(string)
    # Weitere Mapper können hier hinzugefügt werden bei Bedarf
  }))
  default = {}
  
  # Beispiel:
  # protocol_mapper_overrides = {
  #   "public" = {
  #     lhmObjectID_user_attribute = "customObjectID"
  #   }
  # }
}
