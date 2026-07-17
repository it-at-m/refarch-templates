import type { UserInfo } from "@/types/UserInfo";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  getConfig,
} from "@/api/fetch-utils";
import { USERINFO_EMPTY } from "@/types/UserInfo";

/**
 * Retrieves the user data via the userinfo route of the API gateway. The SSO client must be configured so that
 * that the claims offered by Keycloak (see API definition) are correctly delivered in the protocol mapper.
 * You can check which mappers are set in Keycloak UI or the local development stack files under /stack/keycloak.
 * For testdata you might need to create custom user attributes and mappers manually.
 *
 * API-Definition (internal only): https://wiki.muenchen.de/betriebshandbuch/wiki/Red_Hat_Single_Sign-On_(Keycloak)#Scopes
 */
export function getUserInfo(): Promise<UserInfo> {
  return fetch("api/sso/userinfo", getConfig())
    .catch(defaultCatchHandler)
    .then((response) => {
      defaultResponseHandler(
        response,
        "Beim Laden der UserInfo ist ein Fehler aufgetreten."
      );
      return response.json();
    })
    .then((userInfo: Partial<UserInfo>) => {
      return {
        ...USERINFO_EMPTY,
        ...userInfo,
      };
    });
}
