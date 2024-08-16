/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_MUCATAR_URL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
