# TheEntityControllerApi

All URIs are relative to _http://localhost:39146_

| Method                                                                                   | HTTP request                        | Description                        |
| ---------------------------------------------------------------------------------------- | ----------------------------------- | ---------------------------------- |
| [**deleteTheEntity**](TheEntityControllerApi.md#deletetheentity)                         | **DELETE** /theEntity/{theEntityId} | Delete an entity.                  |
| [**getTheEntitiesByPageAndSize**](TheEntityControllerApi.md#gettheentitiesbypageandsize) | **GET** /theEntity                  | Retrieve entities with pagination. |
| [**getTheEntity**](TheEntityControllerApi.md#gettheentity)                               | **GET** /theEntity/{theEntityId}    | Retrieve an entity by its UUID.    |
| [**saveTheEntity**](TheEntityControllerApi.md#savetheentity)                             | **POST** /theEntity                 | Create a new entity.               |
| [**updateTheEntity**](TheEntityControllerApi.md#updatetheentity)                         | **PUT** /theEntity/{theEntityId}    | Update an existing entity.         |

## deleteTheEntity

> deleteTheEntity(theEntityId)

Delete an entity.

Delete an entity. Deletes the entity using the provided UUID.

### Example

```ts
import {
  Configuration,
  TheEntityControllerApi,
} from '';
import type { DeleteTheEntityRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TheEntityControllerApi();

  const body = {
    // string | the UUID of the entity to delete
    theEntityId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
  } satisfies DeleteTheEntityRequest;

  try {
    const data = await api.deleteTheEntity(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

| Name            | Type     | Description                      | Notes                     |
| --------------- | -------- | -------------------------------- | ------------------------- |
| **theEntityId** | `string` | the UUID of the entity to delete | [Defaults to `undefined`] |

### Return type

`void` (Empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

### HTTP response details

| Status code | Description | Response headers |
| ----------- | ----------- | ---------------- |
| **200**     | OK          | -                |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

## getTheEntitiesByPageAndSize

> PagedModelTheEntityResponseDTO getTheEntitiesByPageAndSize(pageNumber, pageSize)

Retrieve entities with pagination.

Retrieve entities with pagination. Fetches a paginated list of entities based on the provided page number and size.

### Example

```ts
import type { GetTheEntitiesByPageAndSizeRequest } from "";

import { Configuration, TheEntityControllerApi } from "";

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TheEntityControllerApi();

  const body = {
    // number | the number of the requested page (default: 0) (optional)
    pageNumber: 56,
    // number | the size of the page to retrieve (default: 10) (optional)
    pageSize: 56,
  } satisfies GetTheEntitiesByPageAndSizeRequest;

  try {
    const data = await api.getTheEntitiesByPageAndSize(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

| Name           | Type     | Description                                    | Notes                         |
| -------------- | -------- | ---------------------------------------------- | ----------------------------- |
| **pageNumber** | `number` | the number of the requested page (default: 0)  | [Optional] [Defaults to `0`]  |
| **pageSize**   | `number` | the size of the page to retrieve (default: 10) | [Optional] [Defaults to `10`] |

### Return type

[**PagedModelTheEntityResponseDTO**](PagedModelTheEntityResponseDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `*/*`

### HTTP response details

| Status code | Description                            | Response headers |
| ----------- | -------------------------------------- | ---------------- |
| **200**     | a page of entities represented as DTOs | -                |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

## getTheEntity

> TheEntityResponseDTO getTheEntity(theEntityId)

Retrieve an entity by its UUID.

Retrieve an entity by its UUID. Fetches the entity details using the provided UUID.

### Example

```ts
import {
  Configuration,
  TheEntityControllerApi,
} from '';
import type { GetTheEntityRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TheEntityControllerApi();

  const body = {
    // string | the UUID of the requested entity
    theEntityId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
  } satisfies GetTheEntityRequest;

  try {
    const data = await api.getTheEntity(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

| Name            | Type     | Description                      | Notes                     |
| --------------- | -------- | -------------------------------- | ------------------------- |
| **theEntityId** | `string` | the UUID of the requested entity | [Defaults to `undefined`] |

### Return type

[**TheEntityResponseDTO**](TheEntityResponseDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `*/*`

### HTTP response details

| Status code | Description                            | Response headers |
| ----------- | -------------------------------------- | ---------------- |
| **200**     | the entity with the given UID as a DTO | -                |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

## saveTheEntity

> TheEntityResponseDTO saveTheEntity(theEntityRequestDTO)

Create a new entity.

Create a new entity. Creates a new entity using the provided entity details.

### Example

```ts
import {
  Configuration,
  TheEntityControllerApi,
} from '';
import type { SaveTheEntityRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TheEntityControllerApi();

  const body = {
    // TheEntityRequestDTO | the details of the entity to create
    theEntityRequestDTO: ...,
  } satisfies SaveTheEntityRequest;

  try {
    const data = await api.saveTheEntity(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

| Name                    | Type                                          | Description                         | Notes |
| ----------------------- | --------------------------------------------- | ----------------------------------- | ----- |
| **theEntityRequestDTO** | [TheEntityRequestDTO](TheEntityRequestDTO.md) | the details of the entity to create |       |

### Return type

[**TheEntityResponseDTO**](TheEntityResponseDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `*/*`

### HTTP response details

| Status code | Description                 | Response headers |
| ----------- | --------------------------- | ---------------- |
| **201**     | the created entity as a DTO | -                |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

## updateTheEntity

> TheEntityResponseDTO updateTheEntity(theEntityId, theEntityRequestDTO)

Update an existing entity.

Update an existing entity. Updates the details of an existing entity using the provided UUID and entity details.

### Example

```ts
import {
  Configuration,
  TheEntityControllerApi,
} from '';
import type { UpdateTheEntityRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TheEntityControllerApi();

  const body = {
    // string | the UUID of the entity to update
    theEntityId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
    // TheEntityRequestDTO | the new details of the entity
    theEntityRequestDTO: ...,
  } satisfies UpdateTheEntityRequest;

  try {
    const data = await api.updateTheEntity(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

| Name                    | Type                                          | Description                      | Notes                     |
| ----------------------- | --------------------------------------------- | -------------------------------- | ------------------------- |
| **theEntityId**         | `string`                                      | the UUID of the entity to update | [Defaults to `undefined`] |
| **theEntityRequestDTO** | [TheEntityRequestDTO](TheEntityRequestDTO.md) | the new details of the entity    |                           |

### Return type

[**TheEntityResponseDTO**](TheEntityResponseDTO.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `*/*`

### HTTP response details

| Status code | Description                 | Response headers |
| ----------- | --------------------------- | ---------------- |
| **200**     | the updated entity as a DTO | -                |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)
