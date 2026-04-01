# Environment-Specific Attribute Overrides

## Übersicht

Dieses Modul unterstützt umgebungsspezifische Überschreibungen von Keycloak-Attribut-Mappings. Dies ist besonders nützlich für Umgebungen ohne LDAP-Anbindung (z.B. `local`, `dev`), wo Dummy-Werte oder alternative Attribute verwendet werden müssen.

## Verwendung

### Standard (mit LDAP)

In Produktions- und Testumgebungen mit LDAP werden die Standard-Attribute verwendet:

```terraform
module "realm_scopes" {
  source = "../../modules/realm-scopes"
  # ... andere Parameter
  
  # Keine attribute_mappings = Standard-Werte werden verwendet
}
```

**Standard-Mappings:**
- `ldap_entry_dn` → `LDAP_ENTRY_DN`

### Ohne LDAP (local, dev)

Für Umgebungen ohne LDAP-Anbindung können alternative Attribute angegeben werden:

```terraform
module "realm_scopes" {
  source = "../../modules/realm-scopes"
  # ... andere Parameter
  
  attribute_mappings = {
    ldap_entry_dn = "ldapEntryDN"  # Alternative für lokale Dummy-Daten
  }
}
```

## Verfügbare Überschreibungen

### `ldap_entry_dn`

- **Standard:** `LDAP_ENTRY_DN`
- **Verwendung:** LDAP Distinguished Name des Benutzers
- **Überschreibung in local/dev:** `ldapEntryDN` (lokales Dummy-Attribut)

**Beispiel:**
```terraform
attribute_mappings = {
  ldap_entry_dn = "ldapEntryDN"
}
```

## Weitere Attribute hinzufügen

### 1. Variable erweitern

In `modules/realm-scopes/variables.tf`:

```terraform
variable "attribute_mappings" {
  description = "Override attribute mappings for environments without LDAP"
  type = object({
    ldap_entry_dn = optional(string, "LDAP_ENTRY_DN")
    member_of     = optional(string, "memberOf")      # NEU
    department    = optional(string, "department")    # NEU
  })
  default = {
    ldap_entry_dn = "LDAP_ENTRY_DN"
    member_of     = "memberOf"
    department    = "department"
  }
}
```

### 2. Mapper anpassen

In `modules/realm-scopes/main.tf`:

```terraform
resource "keycloak_openid_user_attribute_protocol_mapper" "lhm_ext_memberof" {
  for_each            = toset(var.realm_id)
  realm_id            = each.key
  client_scope_id     = keycloak_openid_client_scope.lhm_extended_scope[each.key].id
  name                = "memberof"
  user_attribute      = var.attribute_mappings.member_of  # ← Geändert
  claim_name          = "memberof"
  # ... rest
}
```

### 3. Environment-Config aktualisieren

In `environments/local/main.tf`:

```terraform
attribute_mappings = {
  ldap_entry_dn = "ldapEntryDN"
  member_of     = "localMemberOf"
  department    = "localDepartment"
}
```

## Best Practices

### 1. Konsistente Namenskonvention

- **LDAP (Prod/Test):** `LDAP_ENTRY_DN`, `memberOf` (Original LDAP-Attribute)
- **Local/Dev:** `ldapEntryDN`, `localMemberOf` (camelCase für lokale Dummy-Daten)

### 2. Dokumentation

Dokumentiere jedes überschreibbare Attribut:
- Wo es verwendet wird
- Standard-Wert (mit LDAP)
- Alternative für local/dev
- Welche Claims davon betroffen sind

### 3. Testen

Nach Änderungen immer in allen Umgebungen testen:

```bash
# Local
cd environments/local
terraform plan

# Dev
cd ../dev
terraform plan

# Test (sollte Standard-Werte verwenden)
cd ../test
terraform plan
```

## Beispiel: Vollständige Konfiguration

### Produktion (mit LDAP)

```terraform
# environments/prod/main.tf
module "realm_scopes" {
  source = "../../modules/realm-scopes"
  # ... andere Parameter
  
  # Keine Überschreibung = Standard LDAP-Attribute
}
```

### Local (ohne LDAP)

```terraform
# environments/local/main.tf
module "realm_scopes" {
  source = "../../modules/realm-scopes"
  # ... andere Parameter
  
  attribute_mappings = {
    ldap_entry_dn = "ldapEntryDN"  # Lokales Dummy-Attribut
  }
}
```

## Troubleshooting

### Problem: Attribute nicht gefunden

**Symptom:** Keycloak findet das Attribut nicht im Token

**Lösung:** Prüfe ob:
1. Das Attribut im User-Profil existiert
2. Der Mapper richtig konfiguriert ist
3. Der Scope dem Client zugeordnet ist

### Problem: Falsche Werte in Claims

**Symptom:** Claims enthalten falsche oder leere Werte

**Lösung:**
1. Prüfe die attribute_mappings in der Environment-Config
2. Verifiziere dass die User-Attribute in Keycloak gesetzt sind
3. Checke den Token mit jwt.io

### Problem: Terraform Drift

**Symptom:** Terraform zeigt ständig Änderungen an

**Lösung:** Stelle sicher dass:
- `attribute_mappings` konsistent sind
- Keine manuellen Änderungen in Keycloak gemacht wurden
- Die Variable-Defaults mit der Realität übereinstimmen

## Referenzen

- [Keycloak Protocol Mappers](https://www.keycloak.org/docs/latest/server_admin/#_protocol-mappers)
- [Terraform Keycloak Provider](https://registry.terraform.io/providers/keycloak/keycloak/latest/docs)
