import { fileURLToPath, URL } from "node:url";

import { viteVueCESubStyle } from "@unplugin-vue-ce/sub-style";
import vue from "@vitejs/plugin-vue";
import { defineConfig, loadEnv } from "vite";
import type { PluginOption } from "vite";
import cssInjectedByJsPlugin from "vite-plugin-css-injected-by-js";

const portFromDevelopmentEnv = loadEnv("development", "./")?.VITE_SERVER_PORT;
const port = portFromDevelopmentEnv
  ? Number.parseInt(portFromDevelopmentEnv)
  : 8081;

const apiPortFromDevelopmentEnv = loadEnv("development", "./")?.VITE_API_PORT;
const apiPort = apiPortFromDevelopmentEnv
  ? Number.parseInt(apiPortFromDevelopmentEnv)
  : 8083;

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue({
      features: {
        customElement: true
      }
    }),
    viteVueCESubStyle({}) as PluginOption,
    cssInjectedByJsPlugin(),
  ],
  server: {
    port,
    proxy: {
      "/api": `http://localhost:${apiPort}/`,
      "/actuator": `http://localhost:${apiPort}/`,
      "/clients": `http://localhost:${apiPort}/`,
    },
  },
  resolve: {
    dedupe: ["vue"],
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
    extensions: [".js", ".json", ".jsx", ".mjs", ".ts", ".tsx", ".vue"],
  },
  build: {
    ssrManifest: true,
    manifest: true,
    minify: true,
    outDir: "dist",
    emptyOutDir: false,
    rollupOptions: {
      input: {
        "refarch-hello-world-webcomponent":
          "./src/refarch-hello-world-webcomponent.ts",
      },
      output: {
        entryFileNames: "entry-[name]-[hash].js",
        dir: "dist/src",
      },
    },
  },
  esbuild: {
    drop: ["console", "debugger"],
  },
  define: {
    "process.env": {},
  },
});
