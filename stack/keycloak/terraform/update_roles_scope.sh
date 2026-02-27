#!/usr/bin/env bash
#
# update_roles_scope.sh
#
# Purpose:
#   Ensure the built-in "roles" client scope in specified Keycloak realms includes
#   role claims in the OIDC userinfo endpoint by setting
#   protocol mapper config "userinfo.token.claim" to "true" for realm and client role mappers.
#
# Requirements:
#   - curl
#   - jq
#
# Authentication:
#   Uses Resource Owner Password Credentials against the admin realm with client_id=admin-cli.
#   Provide KEYCLOAK_URL, ADMIN_USER, ADMIN_PASSWORD. Optionally ADMIN_REALM (default: master).
#
# Usage:
#   ./update_roles_scope.sh -k https://keycloak.example.com -u admin -p secret -r master realm1 realm2
#   OR
#   KEYCLOAK_URL=https://keycloak.example.com ADMIN_USER=admin ADMIN_PASSWORD=secret ./update_roles_scope.sh realm1 realm2
#
# Notes:
#   - If your Keycloak disables admin-cli or requires a client secret, adapt get_token() accordingly.
#   - Set INSECURE=true to skip TLS certificate checks (e.g., self-signed certs).
#
set -euo pipefail

# ---------- Configuration / Defaults ----------
ADMIN_REALM=${ADMIN_REALM:-master}
INSECURE=${INSECURE:-false}

# ---------- Logging Helpers ----------
log()  { printf '%s\n' "$*"; }
info() { printf '[INFO] %s\n' "$*"; }
die()  { printf '[ERROR] %s\n' "$*" >&2; exit 1; }

# ---------- Dependency Checks ----------
require_cmd() {
  command -v "$1" >/dev/null 2>&1 || die "Required command '$1' not found. Please install it."
}

# ---------- HTTP (curl-only) Helpers ----------
curl_common_opts() {
  # Common curl options: silent, show errors; add -k if INSECURE=true
  if [ "$INSECURE" = "true" ]; then
    printf '%s' "-sS -k"
  else
    printf '%s' "-sS"
  fi
}

http_post_form() {
  # $1: URL
  # $2: form-encoded body
  local url="$1" body="$2"
  curl $(curl_common_opts) \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -X POST --data "$body" \
    "$url"
}

api_get() {
  # $1: realm
  # $2: path (relative to /admin/realms/<realm>)
  local realm="$1" path="$2"
  curl $(curl_common_opts) -H "Authorization: Bearer $ACCESS_TOKEN" \
    "${KEYCLOAK_URL%/}/admin/realms/${realm}${path}"
}

api_put_json() {
  # $1: realm
  # $2: path (relative to /admin/realms/<realm>)
  # $3: JSON body
  local realm="$1" path="$2" json="$3"
  curl $(curl_common_opts) -X PUT \
    -H "Authorization: Bearer $ACCESS_TOKEN" \
    -H "Content-Type: application/json" \
    --data "$json" \
    "${KEYCLOAK_URL%/}/admin/realms/${realm}${path}" >/dev/null
}

# ---------- Token Retrieval ----------
get_token() {
  # Uses admin-cli with password grant on ADMIN_REALM
  local token_url="${KEYCLOAK_URL%/}/realms/${ADMIN_REALM}/protocol/openid-connect/token"
  local form="client_id=admin-cli&grant_type=password&username=${ADMIN_USER}&password=${ADMIN_PASSWORD}"
  local resp
  resp=$(http_post_form "$token_url" "$form") || die "Failed to request access token"
  ACCESS_TOKEN=$(printf '%s' "$resp" | jq -r '.access_token // empty')
  [ -n "$ACCESS_TOKEN" ] || die "Could not extract access_token. Response: $resp"
}

# ---------- Keycloak Helpers ----------
get_roles_client_scope_id() {
  # $1: realm
  local realm="$1"
  local scopes id
  scopes=$(api_get "$realm" "/client-scopes") || die "Failed to list client scopes for realm '$realm'"
  id=$(printf '%s' "$scopes" | jq -r '.[] | select(.name=="roles") | .id // empty' | head -n1)
  [ -n "$id" ] || die "Client scope 'roles' not found or id missing in realm '$realm'"
  printf '%s' "$id"
}

patch_mapper_userinfo_true() {
  # $1: realm
  # $2: clientScopeId
  # $3: protocolMapper (e.g., oidc-usermodel-realm-role-mapper or oidc-usermodel-client-role-mapper)
  local realm="$1" scope_id="$2" mapper_type="$3"

  local mappers mapper_json mapper_id patched
  mappers=$(api_get "$realm" "/client-scopes/${scope_id}/protocol-mappers/models") || die "Failed to list protocol mappers for scope '$scope_id' in realm '$realm'"

  mapper_json=$(printf '%s' "$mappers" | jq -c --arg t "$mapper_type" '.[] | select(.protocolMapper==$t)')

  if [ -z "$mapper_json" ] || [ "$mapper_json" = "null" ]; then
    info "Mapper '$mapper_type' not found for realm '$realm' (scope '$scope_id'). Skipping."
    return 0
  fi

  mapper_id=$(printf '%s' "$mapper_json" | jq -r '.id // empty')
  [ -n "$mapper_id" ] || die "Mapper id missing for '$mapper_type' in realm '$realm'"

  patched=$(printf '%s' "$mapper_json" | jq -c '(.config |= (. // {})) | .config["userinfo.token.claim"] = "true"')

  api_put_json "$realm" "/client-scopes/${scope_id}/protocol-mappers/models/${mapper_id}" "$patched"
  info "Updated mapper '$mapper_type' in realm '$realm' to include userinfo claims"
}

update_realm_roles_scope() {
  # $1: realm
  local realm="$1"
  info "Processing realm '$realm'"
  local scope_id
  scope_id=$(get_roles_client_scope_id "$realm")

  # Update both realm and client role mappers
  # patch_mapper_userinfo_true "$realm" "$scope_id" "oidc-usermodel-realm-role-mapper"
  patch_mapper_userinfo_true "$realm" "$scope_id" "oidc-usermodel-client-role-mapper"
}

# ---------- Argument Parsing ----------
print_usage() {
  cat <<EOF
Usage: $0 [-k KEYCLOAK_URL] [-u ADMIN_USER] [-p ADMIN_PASSWORD] [-r ADMIN_REALM] realm1 [realm2 ...]

Environment variables (alternatives to flags):
  KEYCLOAK_URL       Base URL, e.g. https://keycloak.example.com
  ADMIN_USER         Admin username
  ADMIN_PASSWORD     Admin password
  ADMIN_REALM        Admin realm (default: master)
  INSECURE           Set to true to skip TLS cert checks (default: false)

Examples:
  $0 -k https://kc -u admin -p secret -r master myrealm
  KEYCLOAK_URL=https://kc ADMIN_USER=admin ADMIN_PASSWORD=secret $0 myrealm1 myrealm2
EOF
}

KEYCLOAK_URL=${KEYCLOAK_URL:-}
ADMIN_USER=${ADMIN_USER:-}
ADMIN_PASSWORD=${ADMIN_PASSWORD:-}

while getopts ":k:u:p:r:h" opt; do
  case $opt in
    k) KEYCLOAK_URL="$OPTARG" ;;
    u) ADMIN_USER="$OPTARG" ;;
    p) ADMIN_PASSWORD="$OPTARG" ;;
    r) ADMIN_REALM="$OPTARG" ;;
    h) print_usage; exit 0 ;;
    \?) die "Invalid option: -$OPTARG" ;;
    :)  die "Option -$OPTARG requires an argument" ;;
  esac
done
shift $((OPTIND-1))

REALMS=("$@")
[ ${#REALMS[@]} -gt 0 ] || die "No realms specified. See -h for usage."

# ---------- Validate & Run ----------
require_cmd curl
require_cmd jq

[ -n "$KEYCLOAK_URL" ]   || die "KEYCLOAK_URL is required"
[ -n "$ADMIN_USER" ]     || die "ADMIN_USER is required"
[ -n "$ADMIN_PASSWORD" ] || die "ADMIN_PASSWORD is required"

info "Using HTTP client: curl"
info "Authenticating against '$KEYCLOAK_URL' (realm: $ADMIN_REALM)"
get_token

# Iterate realms
for realm in "${REALMS[@]}"; do
  update_realm_roles_scope "$realm"
done

info "Done."
