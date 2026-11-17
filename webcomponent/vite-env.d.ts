// see https://vite.dev/guide/env-and-mode#intellisense-for-typescript
interface ViteTypeOptions {
  strictImportMetaEnv: unknown;
}

interface ImportMetaEnv {
  readonly VITE_BASE_API_PATH: string;
  // add more custom env variables
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
