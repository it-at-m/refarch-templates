terraform {
  required_providers {
    keycloak = {
      source  = "keycloak/keycloak"
      version = "~> 5.5.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.6"
    }
  }
}

# Generate random lhmObjectID (9-digit number)
resource "random_integer" "lhm_object_id" {
  count = var.generate_lhm_object_id ? 1 : 0
  min   = 100000000
  max   = 999999999
}

# Generate random LDAP_ID (UUID)
resource "random_uuid" "ldap_id" {}

# Computed values
locals {
  username_lower = lower(var.username)
  cn             = "${lower(var.first_name)}.${lower(var.last_name)}"
  display_name   = "${var.first_name} ${var.last_name}"
  
  # LDAP Entry DN: CN=<username>,OU=Users,OU=ITM,OU=Bereiche,DC=muenchen,DC=de
  ldap_entry_dn = "CN=${local.username_lower},${var.ldap_base_dn}"
  
  # lhmObjectID: generated or custom
  lhm_object_id = var.generate_lhm_object_id ? tostring(random_integer.lhm_object_id[0].result) : var.lhm_object_id
}

# Keycloak User
resource "keycloak_user" "user" {
  realm_id = var.realm_id
  username = local.username_lower
  enabled  = var.enabled

  email          = var.email
  email_verified = var.email_verified
  first_name     = var.first_name
  last_name      = var.last_name

  # Initial password (optional)
  dynamic "initial_password" {
    for_each = var.initial_password != null ? [1] : []
    content {
      value     = var.initial_password
      temporary = var.temporary_password
    }
  }

  # Required actions
  required_actions = var.required_actions

  # User attributes (map(string) - Keycloak Provider Format)
  attributes = var.custom_attributes_enabled == true ? merge(
    {
      lhmObjectID = local.lhm_object_id
      cn          = local.cn
      displayName = local.display_name
      ldapEntryDN = local.ldap_entry_dn
      ldapId      = random_uuid.ldap_id.result
    },
    var.department != "" ? { department = var.department } : {},
    var.telephone_number != "" ? { telephoneNumber = var.telephone_number } : {},
    length(var.member_of) > 0 ? { memberOf = join("##", var.member_of) } : {}
  ) : {}
}
