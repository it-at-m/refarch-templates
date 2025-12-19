output "client_id" {
  description = "Internal Keycloak client ID (UUID)"
  value       = keycloak_openid_client.client.id
}

output "client_secret" {
  description = "Client secret (sensitive)"
  value       = keycloak_openid_client.client.client_secret
  sensitive   = true
}

output "client_roles" {
  description = "Map of created client roles"
  value       = local.all_roles
}

output "service_account_user_id" {
  description = "Service account user ID (if enabled)"
  value       = var.service_accounts_enabled ? keycloak_openid_client.client.service_account_user_id : null
}

output "resource_server_id" {
  description = "Internal client authorization ID (if authorization enabled)"
  value = keycloak_openid_client.client.resource_server_id
}
