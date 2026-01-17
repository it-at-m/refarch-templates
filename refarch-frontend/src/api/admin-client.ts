import { ApiError } from "@/api/ApiError";
import {
  defaultCatchHandler,
  defaultResponseHandler,
  getConfig,
} from "@/api/fetch-utils";

export interface AdminStatusResponse {
  message: string;
}

/**
 * Checks admin access status from the backend.
 * Requires writer role.
 */
export function getAdminStatus(): Promise<AdminStatusResponse> {
  return fetch("api/backend-service/admin/status", getConfig())
    .catch(defaultCatchHandler)
    .then((response) => {
      defaultResponseHandler(
        response,
        "Beim Laden des Admin-Status ist ein Fehler aufgetreten."
      );
      return response
        .json()
        .catch((error) => {
          throw new ApiError(
            "Beim Laden des Admin-Status ist ein Fehler aufgetreten.",
            error
          );
        });
    });
}
