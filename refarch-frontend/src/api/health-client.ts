import { defaultResponseHandler, getConfig } from "@/api/fetch-utils";
import HealthState from "@/types/HealthState";

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
