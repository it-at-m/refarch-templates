import { defaultResponseHandler, getConfig } from "@/api/fetch-utils";

export interface Info {
  application: Application;
}

export interface Application {
  name: string;
  version: string;
}

export function getInfo(): Promise<Info> {
  return fetch("actuator/info", getConfig())
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      defaultResponseHandler(err);
    });
}
