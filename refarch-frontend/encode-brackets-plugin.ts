import * as fs from "node:fs/promises";

import type { Plugin } from "vite";
import type { EditableTreeNode } from "vue-router/unplugin";

/**
 * This plugin ensures parameterized file-based routes (e.g. files like [id].vue) are handled correctly through API gateway
 * when developing locally.
 * The plugin URL encodes brackets in the URL because API gateway (Netty webserver) cannot process brackets directly.
 * Currently, there are some issues open for Vite.js:
 * https://github.com/vitejs/vite/issues/10307
 * https://github.com/vitejs/vite/issues/20799
 *
 * @constructor
 */
export function EncodeBracketsPlugin(): Plugin {
  return {
    name: "vite-plugin-apigateway-bracket-fix",
    enforce: "pre", // Ensures plugin is executed before Vite core plugins, see https://vite.dev/guide/using-plugins#enforcing-plugin-ordering

    // catch imports with encoded brackets and return them
    resolveId(source) {
      if (source.includes("%5B") || source.includes("%5D")) {
        return source;
      }
      return null;
    },

    // load source from accepted filepath (resolveId) and return file from decoded filepath
    async load(id) {
      if (id.includes("%5B") || id.includes("%5D")) {
        const realPath = id.replace(/%5B/gi, "[").replace(/%5D/gi, "]");

        try {
          // let vite watch changes on this file
          this.addWatchFile(realPath);

          return await fs.readFile(realPath, "utf-8");
        } catch {
          // eslint-disable-next-line no-console
          console.error(
            "vite-plugin-apigateway-bracket-fix: Could not load file: ",
            realPath
          );
        }
      }
      return null;
    },
  };
}

export function extendRoute(route: EditableTreeNode): PromiseLike<void> | void {
  // Encode path in order to request with encoded symbols
  if (route.components) {
    for (const [viewName, componentPath] of route.components.entries()) {
      if (componentPath.includes("[") || componentPath.includes("]")) {
        const encodedPath = componentPath
          .replace(/\[/g, "%5B")
          .replace(/\]/g, "%5D");
        route.components.set(viewName, encodedPath);
      }
    }
  }
}
