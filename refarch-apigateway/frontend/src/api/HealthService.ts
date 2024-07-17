import FetchUtils from "@/api/FetchUtils";
import { API_BASE } from "@/Constants";
import HealthState from "@/types/HealthState";

export default class HealthService {
  static checkHealth(): Promise<HealthState> {
    return fetch(`${API_BASE}/actuator/health`, FetchUtils.getGETConfig())
      .then((response) => {
        FetchUtils.defaultResponseHandler(response);
        return response.json();
      })
      .catch((err) => {
        FetchUtils.defaultResponseHandler(err);
      });
  }
}
