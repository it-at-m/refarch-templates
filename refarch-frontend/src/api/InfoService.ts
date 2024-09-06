import FetchUtils from "@/api/FetchUtils";

export interface Info {
  application: Application;
}

export interface Application {
  name: string;
  version: string;
}

export default class InfoService {
  static getInfo(): Promise<Info> {
    return fetch("actuator/info", FetchUtils.getGETConfig())
      .then((response) => {
        FetchUtils.defaultResponseHandler(response);
        return response.json();
      })
      .catch((err) => {
        FetchUtils.defaultResponseHandler(err);
      });
  }
}
