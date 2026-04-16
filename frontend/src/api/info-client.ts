import type { Info } from "@/types/Info";

import { defaultResponseHandler, getConfig } from "@/api/fetch-utils";

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
