# ActuatorApi

All URIs are relative to *http://localhost:39146*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**health**](ActuatorApi.md#health) | **GET** /actuator/health | Actuator web endpoint \&#39;health\&#39; |
| [**info**](ActuatorApi.md#info) | **GET** /actuator/info | Actuator web endpoint \&#39;info\&#39; |
| [**links**](ActuatorApi.md#links) | **GET** /actuator | Actuator root web endpoint |
| [**sbom**](ActuatorApi.md#sbom) | **GET** /actuator/sbom/{id} | Actuator web endpoint \&#39;sbom-id\&#39; |
| [**sboms**](ActuatorApi.md#sboms) | **GET** /actuator/sbom | Actuator web endpoint \&#39;sbom\&#39; |
| [**scrape**](ActuatorApi.md#scrape) | **GET** /actuator/metrics | Actuator web endpoint \&#39;prometheus\&#39; |



## health

> object health()

Actuator web endpoint \&#39;health\&#39;

### Example

```ts
import {
  Configuration,
  ActuatorApi,
} from '';
import type { HealthRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ActuatorApi();

  try {
    const data = await api.health();
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

This endpoint does not need any parameter.

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/vnd.spring-boot.actuator.v3+json`, `application/vnd.spring-boot.actuator.v2+json`, `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## info

> object info()

Actuator web endpoint \&#39;info\&#39;

### Example

```ts
import {
  Configuration,
  ActuatorApi,
} from '';
import type { InfoRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ActuatorApi();

  try {
    const data = await api.info();
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

This endpoint does not need any parameter.

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/vnd.spring-boot.actuator.v3+json`, `application/vnd.spring-boot.actuator.v2+json`, `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## links

> { [key: string]: { [key: string]: Link; }; } links()

Actuator root web endpoint

### Example

```ts
import {
  Configuration,
  ActuatorApi,
} from '';
import type { LinksRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ActuatorApi();

  try {
    const data = await api.links();
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

This endpoint does not need any parameter.

### Return type

**{ [key: string]: { [key: string]: Link; }; }**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/vnd.spring-boot.actuator.v3+json`, `application/vnd.spring-boot.actuator.v2+json`, `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## sbom

> object sbom(id)

Actuator web endpoint \&#39;sbom-id\&#39;

### Example

```ts
import {
  Configuration,
  ActuatorApi,
} from '';
import type { SbomRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ActuatorApi();

  const body = {
    // string
    id: id_example,
  } satisfies SbomRequest;

  try {
    const data = await api.sbom(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | `string` |  | [Defaults to `undefined`] |

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/octet-stream`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **404** | Not Found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## sboms

> object sboms()

Actuator web endpoint \&#39;sbom\&#39;

### Example

```ts
import {
  Configuration,
  ActuatorApi,
} from '';
import type { SbomsRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ActuatorApi();

  try {
    const data = await api.sboms();
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

This endpoint does not need any parameter.

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/vnd.spring-boot.actuator.v3+json`, `application/vnd.spring-boot.actuator.v2+json`, `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## scrape

> object scrape(format, includedNames)

Actuator web endpoint \&#39;prometheus\&#39;

### Example

```ts
import {
  Configuration,
  ActuatorApi,
} from '';
import type { ScrapeRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ActuatorApi();

  const body = {
    // 'CONTENT_TYPE_004' | 'CONTENT_TYPE_OPENMETRICS_100' | 'CONTENT_TYPE_PROTOBUF' (optional)
    format: format_example,
    // string (optional)
    includedNames: includedNames_example,
  } satisfies ScrapeRequest;

  try {
    const data = await api.scrape(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **format** | `CONTENT_TYPE_004`, `CONTENT_TYPE_OPENMETRICS_100`, `CONTENT_TYPE_PROTOBUF` |  | [Optional] [Defaults to `undefined`] [Enum: CONTENT_TYPE_004, CONTENT_TYPE_OPENMETRICS_100, CONTENT_TYPE_PROTOBUF] |
| **includedNames** | `string` |  | [Optional] [Defaults to `undefined`] |

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `text/plain;version=0.0.4;charset=utf-8`, `application/openmetrics-text;version=1.0.0;charset=utf-8`, `application/vnd.google.protobuf;proto=io.prometheus.client.MetricFamily;encoding=delimited`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

