import * as fs from "node:fs";

import type { Plugin } from "vite";

export default function EncodeBracketsPlugin(): Plugin {
  return {
    name: "vite-plugin-netty-bracket-fix",
    enforce: "pre", // Wichtig: Vor allen anderen Plugins laufen

    // catch imports with encoded brackets and return them
    resolveId(source) {
      if (source.includes("%5B") || source.includes("%5D")) {
        return source;
      }
      return null;
    },

    // load source from accepted filepath (resolveId) and return file from decoded filepath
    load(id) {
      if (id.includes("%5B") || id.includes("%5D")) {
        const realPath = id.replace(/%5B/gi, "[").replace(/%5D/gi, "]");

        try {
          // let vite watch changes on this file
          this.addWatchFile(realPath);

          return fs.readFileSync(realPath, "utf-8");
        } catch (e) {
          console.error(
            "Vite Custom Plugin Fehler: Konnte Datei nicht laden:",
            realPath
          );
        }
      }
      return null;
    },
  };
}
