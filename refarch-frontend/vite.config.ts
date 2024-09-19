// Plugins
import { fileURLToPath, URL } from "node:url";

import vue from "@vitejs/plugin-vue";
import ViteFonts from "unplugin-fonts/vite";
import { defineConfig } from "vite";
import vuetify, { transformAssetUrls } from "vite-plugin-vuetify";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue({
      template: { transformAssetUrls },
      features: {
        optionsAPI: false,
      },
    }),
    vuetify(),
    ViteFonts({
      google: {
        families: [
          {
            name: "Roboto",
            styles: "wght@100;300;400;500;700;900",
          },
        ],
      },
    }),
  ],
  server: {
    port: 8081,
    proxy: {
      "/api": "http://localhost:8083",
      "/actuator": "http://localhost:8083",
    },
  },
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
    extensions: [".js", ".json", ".jsx", ".mjs", ".ts", ".tsx", ".vue"],
  },
  build: {
    minify: true,
  },
  define: { "process.env": {} },
});
