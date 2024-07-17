import { API_BASE } from "@/Constants";
import User from "../types/User";
import FetchUtils from "./FetchUtils";

export default class UserService {
  /**
   * Holt die Userdaten über die userinfo Route das API Gateways. Der SSO Client muss so konfiguriert
   * sein, das im Protocol-Mapper richtig die von LHM-KeyCloak angebotenen (siehe API-Definition) claims
   * geliefert werden. Welche Mapper eingestellt sind kann man in KeyCloak kontrollieren. Für Testdaten auf
   * ssodev evtl. custom User Attribute und Mapper anlegen.
   *
   * API-Definition: https://wiki.muenchen.de/betriebshandbuch/wiki/Red_Hat_Single_Sign-On_(Keycloak)#Scopes
   */
  static getUser(): Promise<User> {
    return fetch(`${API_BASE}/api/sso/userinfo`, FetchUtils.getGETConfig())
      .catch(FetchUtils.defaultCatchHandler)
      .then((response) => {
        FetchUtils.defaultResponseHandler(
          response,
          `Beim Laden des Users ist ein Fehler aufgetreten.`
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
}
