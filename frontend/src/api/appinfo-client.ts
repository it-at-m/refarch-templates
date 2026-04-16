import type { AppInfo } from "@/types/AppInfo";

import { defaultResponseHandler, getConfig } from "@/api/fetch-utils";

export function getAppInfo(): Promise<AppInfo> {
  return fetch("actuator/info", getConfig())
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      defaultResponseHandler(err);
    });
}
