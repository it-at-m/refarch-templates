/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_AD2IMAGE_URL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
