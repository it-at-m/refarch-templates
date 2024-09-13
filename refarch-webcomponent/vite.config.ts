import { fileURLToPath, URL } from "node:url";

import vue from "@vitejs/plugin-vue";
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue({
      features: {
        customElement: true,
      },
    }),
  ],
  server: {
    port: 8082,
    proxy: {
      "/api": "http://localhost:8083",
      "/actuator": "http://localhost:8083",
      "/clients": "http://localhost:8083",
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
