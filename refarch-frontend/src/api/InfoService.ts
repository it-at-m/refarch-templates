import FetchUtils from "@/api/FetchUtils";
import { API_BASE } from "@/Constants";

export interface Info {
  application: Application;
  appswitcher: Appswitcher;
}

export interface Application {
  name: string;
  version: string;
}

export interface Appswitcher {
  url: string;
}

export default class InfoService {
  static getInfo(): Promise<Info> {
    return fetch(`${API_BASE}/actuator/info`, FetchUtils.getGETConfig())
      .then((response) => {
        FetchUtils.defaultResponseHandler(response);
        return response.json();
      })
      .catch((err) => {
        FetchUtils.defaultResponseHandler(err);
      });
  }
}
