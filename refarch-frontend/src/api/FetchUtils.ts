import { ApiError, Levels } from "@/api/error";

export default class FetchUtils {
  /**
   * Returns a default GET-Config for fetch
   */
  static getGETConfig(): RequestInit {
    return {
      headers: this.getHeaders(),
      mode: "cors",
      credentials: "same-origin",
      redirect: "manual",
    };
  }

  /**
   * Returns a default POST-Config for fetch
   * @param body Optional body to be transferred
   */
  // eslint-disable-next-line
  static getPOSTConfig(body: any): RequestInit {
    return {
      method: "POST",
      body: body ? JSON.stringify(body) : undefined,
      headers: FetchUtils.getHeaders(),
      mode: "cors",
      credentials: "same-origin",
      redirect: "manual",
    };
  }

  /**
   * Returns a default PUT-Config for fetch
   * If available, the version of the entity to be updated is included in this as an "If-Match" header.
   * @param body Optional body to be transferred
   */
  // eslint-disable-next-line
  static getPUTConfig(body: any): RequestInit {
    const headers = FetchUtils.getHeaders();
    if (body.version) {
      headers.append("If-Match", body.version);
    }
    return {
      method: "PUT",
      body: body ? JSON.stringify(body) : undefined,
      headers,
      mode: "cors",
      credentials: "same-origin",
      redirect: "manual",
    };
  }

  /**
   * Returns a default PATCH-Config for fetch
   * If available, the version of the entity to be updated is included in this as an "If-Match" header.
   * @param body Optional body to be transferred
   */
  // eslint-disable-next-line
  static getPATCHConfig(body: any): RequestInit {
    const headers = FetchUtils.getHeaders();
    if (body.version !== undefined) {
      headers.append("If-Match", body.version);
    }
    return {
      method: "PATCH",
      body: body ? JSON.stringify(body) : undefined,
      headers,
      mode: "cors",
      credentials: "same-origin",
      redirect: "manual",
    };
  }

  /**
   * Covers the default handling of a response. This includes:
   *
   * - Error with missing authorizations --> HTTP 403
   * - Reload the app at session timeout --> HTTP 3xx
   * - Default error for all HTTP codes nicht 2xx
   *
   * @param response The response from the fetch command to be checked.
   * @param errorMessage The error message to be displayed for an HTTP code != 2xx.
   */
  static defaultResponseHandler(
    response: Response,
    errorMessage = "An unknown error has occurred."
  ): void {
    if (!response.ok) {
      if (response.status === 403) {
        throw new ApiError({
          level: Levels.ERROR,
          message:
            "You do not have the necessary rights to perform this action.",
        });
      } else if (response.type === "opaqueredirect") {
        location.reload();
      }
      throw new ApiError({
        level: Levels.WARNING,
        message: errorMessage,
      });
    }
  }

  /**
   * Default catch handler for all service requests.
   * Currently only throws an ApiError
   * @param error The error object from fetch command
   * @param errorMessage The error message to be included in the ApiError object.
   */
  static defaultCatchHandler(
    error: Error,
    errorMessage = "An unknown error has occurred."
  ): PromiseLike<never> {
    throw new ApiError({
      level: Levels.WARNING,
      message: errorMessage,
    });
  }

  /**
   * Builds the headers for the request.
   * @returns {Headers}
   */
  static getHeaders(): Headers {
    const headers = new Headers({
      "Content-Type": "application/json",
    });
    const csrfCookie = this._getXSRFToken();
    if (csrfCookie !== "") {
      headers.append("X-XSRF-TOKEN", csrfCookie);
    }
    return headers;
  }

  /**
   * Returns the XSRF-TOKEN.
   * @returns {string|string}
   */
  static _getXSRFToken(): string {
    const help = document.cookie.match(
      "(^|;)\\s*" + "XSRF-TOKEN" + "\\s*=\\s*([^;]+)"
    );
    return (help ? help.pop() : "") as string;
  }
}
