variable "realm_id" {
  description = "Keycloak Realm ID"
  type        = string
}

variable "username" {
  description = "Username (lowercase, will be auto-converted)"
  type        = string
  validation {
    condition     = can(regex("^[a-z0-9._-]+$", var.username))
    error_message = "Username must be lowercase alphanumeric with dots, underscores, or hyphens."
  }
}

variable "email" {
  description = "User email address"
  type        = string
  validation {
    condition     = can(regex("^[^@]+@[^@]+\\.[^@]+$", var.email))
    error_message = "Must be a valid email address."
  }
}

variable "first_name" {
  description = "First name"
  type        = string
}

variable "last_name" {
  description = "Last name"
  type        = string
}

variable "enabled" {
  description = "Enable user account"
  type        = bool
  default     = true
}

variable "email_verified" {
  description = "Mark email as verified"
  type        = bool
  default     = false
}

variable "initial_password" {
  description = "Initial password (optional, will be temporary if set)"
  type        = string
  sensitive   = true
  default     = null
}

variable "temporary_password" {
  description = "Set initial password as temporary (user must change on first login)"
  type        = bool
  default     = true
}

# LDAP/AD Attributes
variable "department" {
  description = "Department/Organizational Unit"
  type        = string
  default     = ""
}

variable "telephone_number" {
  description = "Telephone number"
  type        = string
  default     = ""
}

variable "member_of" {
  description = "List of group DNs (memberOf attribute)"
  type        = list(string)
  default     = []
}

variable "ldap_base_dn" {
  description = "LDAP Base DN (default: OU=Users,OU=ITM,OU=Bereiche,DC=muenchen,DC=de)"
  type        = string
  default     = "OU=Users,OU=ITM,OU=Bereiche,DC=muenchen,DC=de"
}

variable "generate_lhm_object_id" {
  description = "Auto-generate lhmObjectID (9-digit number)"
  type        = bool
  default     = true
}

variable "lhm_object_id" {
  description = "Custom lhmObjectID (if generate_lhm_object_id = false)"
  type        = string
  default     = null
  validation {
    condition     = var.lhm_object_id == null || can(regex("^[0-9]{9}$", var.lhm_object_id))
    error_message = "lhmObjectID must be exactly 9 digits."
  }
}

variable "required_actions" {
  description = "Required actions for user (e.g., UPDATE_PASSWORD, VERIFY_EMAIL)"
  type        = list(string)
  default     = []
}