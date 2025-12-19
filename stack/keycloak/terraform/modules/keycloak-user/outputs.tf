output "user_id" {
  description = "Keycloak User ID (UUID)"
  value       = keycloak_user.user.id
}

output "username" {
  description = "Username (lowercase)"
  value       = keycloak_user.user.username
}

output "lhm_object_id" {
  description = "Generated or custom lhmObjectID"
  value       = local.lhm_object_id
}

output "ldap_id" {
  description = "Generated LDAP_ID (UUID)"
  value       = random_uuid.ldap_id.result
}

output "ldap_entry_dn" {
  description = "LDAP Entry DN"
  value       = local.ldap_entry_dn
}

output "cn" {
  description = "Common Name (cn)"
  value       = local.cn
}

output "display_name" {
  description = "Display Name"
  value       = local.display_name
}