terraform {
  required_providers {
    keycloak = {
      source  = "keycloak/keycloak"
      version = "5.7.0"
    }
  }
}

provider "keycloak" {
  url       = "http://keycloak:8100/auth"
  username  = "admin"
  password  = "admin"
  client_id = "admin-cli"
}
