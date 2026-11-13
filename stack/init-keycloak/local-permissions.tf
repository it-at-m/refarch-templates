# can be commented in to generate example permissions
# also requires commenting in authorization in client

# # resources
# resource "keycloak_openid_client_authorization_resource" "the_entity_read" {
#   realm_id           = keycloak_realm.local.id
#   resource_server_id = module.client_local.resource_server_id
#   name               = "REFARCH_THEENTITY_READ"
# }
#
# resource "keycloak_openid_client_authorization_resource" "the_entity_write" {
#   realm_id           = keycloak_realm.local.id
#   resource_server_id = module.client_local.resource_server_id
#   name               = "REFARCH_THEENTITY_WRITE"
# }
#
# resource "keycloak_openid_client_authorization_resource" "the_entity_delete" {
#   realm_id           = keycloak_realm.local.id
#   resource_server_id = module.client_local.resource_server_id
#   name               = "REFARCH_THEENTITY_DELETE"
# }
#
# # policies
# resource "keycloak_openid_client_role_policy" "role_writer" {
#   realm_id           = keycloak_realm.local.id
#   resource_server_id = module.client_local.resource_server_id
#   name               = "role_writer"
#   type               = "role"
#   logic              = "POSITIVE"
#   decision_strategy  = "AFFIRMATIVE"
#   role {
#     id       = module.client_local.client_roles["writer"]
#     required = false
#   }
# }
# resource "keycloak_openid_client_role_policy" "role_reader" {
#   realm_id           = keycloak_realm.local.id
#   resource_server_id = module.client_local.resource_server_id
#   name               = "role_reader"
#   type               = "role"
#   logic              = "POSITIVE"
#   decision_strategy  = "AFFIRMATIVE"
#   role {
#     id       = module.client_local.client_roles["reader"]
#     required = false
#   }
# }
#
# # permissions
# resource "keycloak_openid_client_authorization_permission" "the_entity_delete" {
#   realm_id           = keycloak_realm.local.id
#   resource_server_id = module.client_local.resource_server_id
#   name               = "permission_REFARCH_THEENTITY_DELETE"
#   type               = "resource"
#   decision_strategy  = "AFFIRMATIVE"
#   resources          = [keycloak_openid_client_authorization_resource.the_entity_delete.id]
#   policies           = [keycloak_openid_client_role_policy.role_writer.id]
# }
#
# resource "keycloak_openid_client_authorization_permission" "the_entity_write" {
#   realm_id           = keycloak_realm.local.id
#   resource_server_id = module.client_local.resource_server_id
#   name               = "permission_REFARCH_THEENTITY_WRITE"
#   type               = "resource"
#   decision_strategy  = "AFFIRMATIVE"
#   resources          = [keycloak_openid_client_authorization_resource.the_entity_write.id]
#   policies           = [keycloak_openid_client_role_policy.role_writer.id]
# }
#
# resource "keycloak_openid_client_authorization_permission" "the_entity_read" {
#   realm_id           = keycloak_realm.local.id
#   resource_server_id = module.client_local.resource_server_id
#   name               = "permission_REFARCH_THEENTITY_READ"
#   type               = "resource"
#   decision_strategy  = "AFFIRMATIVE"
#   resources          = [keycloak_openid_client_authorization_resource.the_entity_read.id]
#   policies = [
#     keycloak_openid_client_role_policy.role_writer.id,
#     keycloak_openid_client_role_policy.role_reader.id,
#   ]
# }
