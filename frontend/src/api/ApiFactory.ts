import { getSecurityHeaders } from "@/api/fetch-utils.ts";
import { BaseAPI, Configuration } from "@/api/generated/refarch-backend";
import { BASE_API_PATH } from "@/constants.ts";

type ApiCtor<T extends BaseAPI> = new (config: Configuration) => T;

const instances = new Map<ApiCtor<BaseAPI>, BaseAPI>();

async function customFetch(url: string, init?: RequestInit) {
  const customInit: RequestInit = {
    ...init,
    mode: "cors",
    credentials: "same-origin",
    redirect: "manual",
  };

  return fetch(url, customInit);
}

function createConfig(): Configuration {
  return new Configuration({
    basePath: BASE_API_PATH,
    fetchApi: customFetch,
    middleware: [
      {
        pre: async (context) => {
          return {
            url: context.url,
            init: {
              ...context.init,
              headers: { ...context.init.headers, ...getSecurityHeaders() },
            },
          };
        },
      },
    ],
  });
}

/**
 * Retrieves the instance of the given OpenAPI class or creates a new one if none exists.
 * This factory pattern makes sure that only one instance of each OpenAPI-class exists (singleton like).
 * @param ApiClass the OpenAPI class to instantiate / retrieve
 */
function getInstance<T extends BaseAPI>(ApiClass: ApiCtor<T>): T {
  const existing = instances.get(ApiClass);
  if (existing) {
    return existing as T;
  }

  const api = new ApiClass(createConfig());
  instances.set(ApiClass, api);
  return api;
}

export const ApiFactory = {
  getInstance,
} as const;
