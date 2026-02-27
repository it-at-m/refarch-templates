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
  type        = string
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

variable "use_custom_authorities_mapper" {
  description = "Whether to use the custom 'oidc-authorities-mapper' protocol mapper (requires plugin installed)."
  type        = bool
  default     = true
}

variable "lhmObjectID_attribute" {
  description = "Override for lhmObjectID attribute"
  type = string
  default = "lhmObjectID"
}
