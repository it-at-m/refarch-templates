import { fileURLToPath } from "node:url";

import { defineConfig, mergeConfig } from "vitest/config";

import viteConfig from "./vite.config";

export default defineConfig((configEnv) =>
  mergeConfig(
    viteConfig(configEnv),
    defineConfig({
      test: {
        environment: "jsdom",
        root: fileURLToPath(new URL("./", import.meta.url)),
        server: {
          deps: {
            inline: ["vuetify"],
          },
        },
      },
    })
  )
);
