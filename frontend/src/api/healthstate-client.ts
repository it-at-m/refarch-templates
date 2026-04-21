import type { HealthState } from "@/types/HealthState";

import { defaultResponseHandler, getConfig } from "@/api/fetch-utils";

export function checkHealth(): Promise<HealthState> {
  return fetch("actuator/health", getConfig())
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      defaultResponseHandler(err);
    });
}
