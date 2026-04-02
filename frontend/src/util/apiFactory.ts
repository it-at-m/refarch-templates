import type { HTTPHeaders } from "@/api/generated/refarch-backend";

import { BaseAPI, Configuration } from "@/api/generated/refarch-backend";
import { getHeaders } from "@/api/fetch-utils.ts";

type ApiCtor<T extends BaseAPI> = new (config: Configuration) => T;

const instances = new Map<ApiCtor<BaseAPI>, BaseAPI>();

function createConfig(): Configuration {
  return new Configuration({
    basePath: "/api/backend",
    credentials: "same-origin",
    headers: convertHeaders(getHeaders()),
  });
}

/**
 * Retrieves the instance of the given OpenAPI class or creates a new one if none exists.
 * This factory pattern makes sure that only one instance of each OpenAPI-class exists (singleton like).
 * @param ApiClass the OpenAPI class to instantiate / retrieve
 */
function getInstance<T extends BaseAPI>(ApiClass: ApiCtor<T>): T {
  const existing = instances.get(ApiClass as ApiCtor<BaseAPI>);
  if (existing) {
    return existing as T;
  }

  const api = new ApiClass(createConfig());
  instances.set(ApiClass as ApiCtor<BaseAPI>, api);
  return api;
}

/**
 * Converts a Headers object into a simple key-value pair object.
 * @param {Headers} headers - The headers object to be converted.
 * @returns {HTTPHeaders} An object with the same headers.
 */
function convertHeaders(headers: Headers): HTTPHeaders {
  const httpHeaders: HTTPHeaders = {};
  headers.forEach((value, key) => {
    httpHeaders[key] = value;
  });
  return httpHeaders;
}

export const ApiFactory = {
  getInstance,
} as const;
