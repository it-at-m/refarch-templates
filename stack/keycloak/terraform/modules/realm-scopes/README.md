# Realm Scopes Module

This Terraform module manages OpenID Connect Client Scopes for Keycloak realms in the LHM (Landeshauptstadt München) infrastructure.

## Overview

The module creates and manages:
- **Default Scopes**: Automatically included in all tokens
- **Optional Scopes**: Must be explicitly requested by clients
- **Protocol Mappers**: Map user attributes to token claims

## Table of Contents

- [Default Scopes](#default-scopes)
- [Optional Scopes](#optional-scopes)
- [Deprecated Scopes](#deprecated-scopes)
- [Scope Details](#scope-details)
- [Usage](#usage)
- [Variables](#variables)

---

## Default Scopes

These scopes are **automatically included** in all tokens issued by the realm.

### `lhm-core` (Default Scope)

**Description**: LHM standard scope – Contains all user attributes typical for City of Munich applications.

**Configuration**:
- `include_in_token_scope`: `true`
- Automatically assigned to all new clients in the realm

**Protocol Mappers**:

| Mapper Name | User Attribute | Claim Name | Type | ID Token | Access Token | UserInfo |
|-------------|---------------|------------|------|----------|--------------|----------|
| lhmObjectID | `lhmObjectID` | `lhmObjectID` | String | ✅ | ✅ | ✅ |
| department | `department` | `department` | String | ✅ | ✅ | ✅ |
| telephoneNumber | `telephoneNumber` | `telephone_number` | String | ✅ | ✅ | ✅ |

**Example Token Claims**:
```json
{
  "lhmObjectID": "12345678",
  "department": "IT-Referat",
  "telephone_number": "+49 89 233-12345"
}
```

---

## Optional Scopes

These scopes must be **explicitly requested** by the client application (e.g., `scope=openid profile email lhm-core`).

### Standard Keycloak Scopes

These are built-in Keycloak scopes used by the module:

#### `profile`
Standard OpenID Connect scope for basic profile information.

**Default Claims** (managed by Keycloak):
- `name`
- `family_name`
- `given_name`
- `middle_name`
- `nickname`
- `preferred_username`
- `profile`
- `picture`
- `website`
- `gender`
- `birthdate`
- `zoneinfo`
- `locale`
- `updated_at`

#### `email`
Standard OpenID Connect scope for email information.

**Default Claims** (managed by Keycloak):
- `email`
- `email_verified`

#### `roles`
OpenID Connect scope for user roles.

**Configuration**:
- `include_in_token_scope`: `true`
- Conditionally managed: Either created as resource or referenced as data source based on `manage_roles_scope` variable

**Protocol Mappers**:

| Mapper Name | Type | Claim Name | Multivalued | ID Token | Access Token | UserInfo |
|-------------|------|------------|-------------|----------|--------------|----------|
| client roles | User Client Role | `resource_access.${client_id}.roles` | ✅ | ❌ | ✅ | ✅ |

**Example Token Claims**:
```json
{
  "resource_access": {
    "my-application": {
      "roles": ["admin", "user", "viewer"]
    }
  }
}
```

#### `acr`
Authentication Context Class Reference scope.

**Default Claims** (managed by Keycloak):
- `acr` (Authentication Context Class Reference)

#### `web-origins`
Scope for CORS (Cross-Origin Resource Sharing) configuration.

**Default Claims** (managed by Keycloak):
- `allowed-origins` (List of allowed CORS origins)

#### `basic`
Basic Keycloak scope for minimal token information.

**Default Claims** (managed by Keycloak):
- Basic subject and issuer information

### Custom LHM Scopes

Additional custom scopes specific to LHM can be configured via the `optional_scopes` variable.

---

## Deprecated Scopes

⚠️ **These scopes are maintained for backward compatibility only. Do NOT use them in new applications.**

### `LHM` (Deprecated)

**Replacement**: Use `lhm-core` instead

**Description**: Legacy scope for backward compatibility. This scope will be removed in a future release.

**Configuration**:
- `include_in_token_scope`: `true`

**Protocol Mappers**:

| Mapper Name | User Attribute/Property | Claim Name | Type | ID Token | Access Token | UserInfo |
|-------------|------------------------|------------|------|----------|--------------|----------|
| telephoneNumber | `telephoneNumber` (attr) | `telephoneNumber` | String | ✅ | ✅ | ✅ |
| surname | `lastName` (attr) | `surname` | String | ✅ | ✅ | ✅ |
| givenname | `firstName` (property) | `given_name` | String | ✅ | ✅ | ✅ |
| department | `department` (attr) | `department` | String | ✅ | ✅ | ✅ |
| displayName | `displayName` (attr) | `name` | String | ✅ | ✅ | ✅ |
| lhmObjectID | `lhmObjectID` (attr) | `lhmObjectID` | String | ✅ | ✅ | ✅ |
| username | `Username` (property) | `username` | String | ✅ | ✅ | ✅ |
| email | `email` (property) | `email` | String | ✅ | ✅ | ✅ |

### `LHM_Extended` (Deprecated)

**Replacement**: Use `lhm-core` and standard scopes instead

**Description**: Extended legacy scope for backward compatibility. This scope will be removed in a future release.

**Configuration**:
- `include_in_token_scope`: `true`

**Protocol Mappers**:

| Mapper Name | Type | User Attribute | Claim Name | Multivalued | ID Token | Access Token | UserInfo |
|-------------|------|---------------|------------|-------------|----------|--------------|----------|
| dn | User Attribute | `LDAP_ENTRY_DN` (env-aware*) | `dn` | ❌ | ✅ | ✅ | ✅ |
| User Authorities mapper | Generic (oidc-authorities-mapper) | - | `authorities` | ✅ | ✅ | ❌ | ✅ |
| memberof | User Attribute | `memberOf` | `memberof` | ✅ | ✅ | ✅ | ✅ |
| acr loa level | Hardcoded Claim | - | `acr` | ❌ | ✅ | ❌ | ❌ |
| audience resolve | Audience Resolve | - | - | - | - | - | - |
| client roles | User Client Role | - | `resource_access.${client_id}.roles` | ✅ | ❌ | ✅ | ✅ |

*Environment-aware: The `dn` mapper uses `var.attribute_mappings.ldap_entry_dn` which can differ between environments.

**Example Token Claims**:
```json
{
  "dn": "CN=Max Mustermann,OU=Users,DC=muenchen,DC=de",
  "authorities": ["ROLE_ADMIN", "ROLE_USER"],
  "memberof": [
    "CN=IT-Team,OU=Groups,DC=muenchen,DC=de",
    "CN=Developers,OU=Groups,DC=muenchen,DC=de"
  ],
  "acr": "loa-1",
  "resource_access": {
    "my-app": {
      "roles": ["admin"]
    }
  }
}
```

---

## Scope Details

### Protocol Mapper Types

The module uses several types of protocol mappers:

1. **User Attribute Mapper** (`keycloak_openid_user_attribute_protocol_mapper`)
   - Maps custom user attributes to token claims
   - Example: `lhmObjectID`, `department`, `telephoneNumber`

2. **User Property Mapper** (`keycloak_openid_user_property_protocol_mapper`)
   - Maps built-in Keycloak user properties to token claims
   - Example: `firstName`, `email`, `username`

3. **User Client Role Mapper** (`keycloak_openid_user_client_role_protocol_mapper`)
   - Maps client-specific roles to token claims
   - Supports multivalued claims

4. **Generic Mapper** (`keycloak_generic_protocol_mapper`)
   - Custom protocol mappers with specific configurations
   - Example: `oidc-authorities-mapper`

5. **Hardcoded Claim Mapper** (`keycloak_openid_hardcoded_claim_protocol_mapper`)
   - Adds static values to tokens
   - Example: `acr` with value `loa-1`

6. **Audience Resolve Mapper** (`keycloak_openid_audience_resolve_protocol_mapper`)
   - Automatically resolves audience claims for client roles

### Token Placement

Claims can be included in three different token types:

- **ID Token** (`add_to_id_token`): OpenID Connect identity token (contains user information)
- **Access Token** (`add_to_access_token`): OAuth 2.0 access token (for API authorization)
- **UserInfo** (`add_to_userinfo`): UserInfo endpoint response (via `/protocol/openid-connect/userinfo`)

---

## Usage

### Basic Example

```hcl
module "realm_scopes" {
  source = "./modules/realm-scopes"

  realm_id           = ["public", "internal", "test"]
  client_id          = var.client_id
  client_secret      = var.client_secret
  keycloak_url       = "https://sso.muenchen.de/auth"
  realm              = "master"
  
  # Optional custom scopes
  optional_scopes = ["offline_access", "microprofile-jwt"]
  
  # Roles scope management
  manage_roles_scope = true  # Create roles scope (for new realms)
  # manage_roles_scope = false  # Use existing roles scope (for existing realms)
  
  # Environment-aware LDAP attribute mapping
  attribute_mappings = {
    ldap_entry_dn = "LDAP_ENTRY_DN"  # or "entryDN" for different environments
  }
}
```

### Client Configuration Example

When configuring a client to use these scopes:

```hcl
resource "keycloak_openid_client" "my_app" {
  realm_id  = "public"
  client_id = "my-application"
  
  # Default scopes (automatically included)
  # - lhm-core
  
  # Optional scopes that can be requested
  # Add specific optional scopes needed by your application
}

# Assign default scopes
resource "keycloak_openid_client_default_scopes" "my_app_default" {
  realm_id  = "public"
  client_id = keycloak_openid_client.my_app.id
  
  default_scopes = [
    "lhm-core"  # Automatically managed by realm-scopes module
  ]
}

# Assign optional scopes
resource "keycloak_openid_client_optional_scopes" "my_app_optional" {
  realm_id  = "public"
  client_id = keycloak_openid_client.my_app.id
  
  optional_scopes = [
    "profile",
    "email",
    "roles"
  ]
}
```

### Application Token Request Example

```bash
# Request token with optional scopes
curl -X POST "https://sso.muenchen.de/auth/realms/public/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "client_id=my-application" \
  -d "client_secret=secret" \
  -d "code=AUTH_CODE" \
  -d "scope=openid profile email lhm-core roles"
```

---

## Variables

### Required Variables

| Variable | Type | Description |
|----------|------|-------------|
| `realm_id` | `list(string)` | List of realm IDs to manage scopes for |
| `client_id` | `string` | Keycloak admin client ID |
| `client_secret` | `string` | Keycloak admin client secret |
| `keycloak_url` | `string` | Keycloak base URL |
| `realm` | `string` | Admin realm name (usually "master") |

### Optional Variables

| Variable | Type | Default | Description |
|----------|------|---------|-------------|
| `optional_scopes` | `list(string)` | `["LHM", "LHM_Extended"]` | Custom optional scopes to add |
| `manage_roles_scope` | `bool` | `true` | Whether to create roles scope as resource (true) or use existing (false) |
| `skip_default_scopes_lookup` | `bool` | `false` | Skip lookup of default Keycloak scopes (for new realms) |
| `realm_blacklist` | `list(string)` | `[]` | Realms to exclude from scope management |
| `attribute_mappings` | `object` | `{ ldap_entry_dn = "LDAP_ENTRY_DN" }` | Environment-specific attribute mappings |

---

## Outputs

The module does not currently expose outputs. Scope IDs are managed internally.

---

## Migration Guide

### From Legacy Scopes to lhm-core

If you're using the deprecated `LHM` or `LHM_Extended` scopes:

1. **Update Client Configuration**: Change default scopes from `LHM` to `lhm-core`
2. **Update Application Code**: Map claim names to new format:
   - `surname` → Use standard `family_name` from `profile` scope
   - `given_name` → Already standard in `profile` scope
   - `name` (from displayName) → Standard in `profile` scope
   - `telephoneNumber` → Now `telephone_number` in `lhm-core`
   - Other attributes remain the same

3. **Test Thoroughly**: Verify all claims are present in tokens before removing legacy scopes

---

## Best Practices

1. **Use lhm-core for new applications**: Don't use deprecated `LHM` or `LHM_Extended` scopes
2. **Request minimal scopes**: Only request scopes your application actually needs
3. **Use standard scopes when possible**: Prefer `profile` and `email` over custom mappers
4. **Document scope requirements**: Clearly document which scopes your application requires
5. **Test with minimal permissions**: Ensure your application works with only required scopes

---

## Troubleshooting

### Missing Claims in Token

**Problem**: Expected claims are missing from access token or ID token

**Solutions**:
1. Verify the scope is requested in the authentication request
2. Check if the scope is assigned as default or optional scope to the client
3. Verify protocol mapper configuration (`add_to_id_token`, `add_to_access_token`)
4. Check if user has the required attributes populated in Keycloak

### Roles Scope Conflicts

**Problem**: Terraform detects drift or fails to create roles scope

**Solutions**:
1. For **existing realms**: Set `manage_roles_scope = false` to use existing scope
2. For **new realms**: Set `manage_roles_scope = true` to create scope
3. Check if roles scope already exists in realm before running terraform

### LDAP Attribute Mapping Issues

**Problem**: `dn` claim shows wrong attribute or is missing

**Solutions**:
1. Verify `attribute_mappings.ldap_entry_dn` matches your LDAP configuration
2. Common values: `LDAP_ENTRY_DN`, `entryDN`, `ldapEntryDn`
3. Check LDAP user federation mapper configuration in Keycloak

---

## Related Documentation

- [Keycloak Client Scopes Documentation](https://www.keycloak.org/docs/latest/server_admin/#_client_scopes)
- [OpenID Connect Core Specification](https://openid.net/specs/openid-connect-core-1_0.html)
- [OAuth 2.0 Scopes](https://datatracker.ietf.org/doc/html/rfc6749#section-3.3)

---

## Version History

- **v1.0**: Initial module with lhm-core scope
- **v0.9**: Legacy LHM and LHM_Extended scopes (deprecated)

---

## Support

For issues or questions regarding this module:
- Check existing Terraform state for conflicts
- Review Keycloak Admin Console for manual changes
- Consult LHM Infrastructure team for environment-specific configurations
