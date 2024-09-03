import FetchUtils from "@/api/FetchUtils";
import HealthState from "@/types/HealthState";

export default class HealthService {
  static checkHealth(): Promise<HealthState> {
    return fetch("actuator/health", FetchUtils.getGETConfig())
      .then((response) => {
        FetchUtils.defaultResponseHandler(response);
        return response.json();
      })
      .catch((err) => {
        FetchUtils.defaultResponseHandler(err);
      });
  }
}
