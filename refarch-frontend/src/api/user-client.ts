import {
  defaultCatchHandler,
  defaultResponseHandler,
  getConfig,
} from "@/api/fetch-utils";
import User from "@/types/User";

/**
 * Retrieves the user data via the userinfo route of the API gateway. The SSO client must be configured so that
 * that the claims offered by Keycloak (see API definition) are correctly delivered in the protocol mapper.
 * You can check which mappers are set in Keycloak UI or the local development stack files under /stack/keycloak.
 * For testdata you might need to create custom user attributes and mappers manually.
 *
 * API-Definition (internal only): https://wiki.muenchen.de/betriebshandbuch/wiki/Red_Hat_Single_Sign-On_(Keycloak)#Scopes
 */
export function getUser(): Promise<User> {
  return fetch("api/sso/userinfo", getConfig())
    .catch(defaultCatchHandler)
    .then((response) => {
      defaultResponseHandler(
        response,
        "Beim Laden des Users ist ein Fehler aufgetreten."
      );
      return response.json();
    })
    .then((json: Partial<User>) => {
      const u = new User();
      u.sub = json.sub || "";

      // LHM
      u.displayName = json.displayName || "";
      u.surname = json.surname || "";
      u.telephoneNumber = json.telephoneNumber || "";
      u.email = json.email || "";
      u.username = json.username || "";
      u.givenname = json.givenname || "";
      u.department = json.department || "";
      u.lhmObjectID = json.lhmObjectID || "";

      // LHM_Extended
      u.preferred_username = json.preferred_username || "";
      u.memberof = json.memberof || [];
      u.user_roles = json.user_roles || [];
      u.authorities = json.authorities || [];
      return u;
    });
}
