import type { Plugin } from "vite";

export default function EncodeBracketsPlugin(): Plugin {
  return {
    name: "vite-plugin-netty-bracket-fix",
    // WICHTIG: 'post' stellt sicher, dass Vite intern seine Dateien mit [] findet,
    // BEVOR wir den Code für den Browser anfassen.
    enforce: "post",

    // 1. TRANSFORM: Manipuliert nur den JavaScript-Code, der an den Browser geht
    transform(code, id) {
      if (code.includes("[") || code.includes("]")) {
        // Dieses Regex sucht gezielt nach Dateipfaden in Anführungszeichen,
        // die ".vue" beinhalten (z. B. import("/src/routes/test/[id].vue"))
        return code.replace(
          /(['"])([^'"]*\.vue[^'"]*)(['"])/g,
          (match, openQuote, pathString, closeQuote) => {
            if (pathString.includes("[") || pathString.includes("]")) {
              console.log("##code" + match);
              // Wir encodieren nur die URL-Strings im Code
              const encodedPath = pathString
                .replace(/\[/g, "%5B")
                .replace(/\]/g, "%5D");
              console.log("##result" + encodedPath);
              return openQuote + encodedPath + closeQuote;
            }
            return match;
          }
        );
      }
    },

    // 2. MIDDLEWARE: Fängt den Request vom Browser/Netty ab und decodiert ihn für Vite
    configureServer(server) {
      server.middlewares.use((req, res, next) => {
        console.log(req.url);
        if (req.url && (req.url.includes("%5B") || req.url.includes("%5D"))) {
          // Mach aus dem %5B wieder ein [, damit Vites Dateiserver die Datei ausliefern kann
          req.url = req.url.replace(/%5B/gi, "[").replace(/%5D/gi, "]");

          if (req.originalUrl) {
            req.originalUrl = req.originalUrl
              .replace(/%5B/gi, "[")
              .replace(/%5D/gi, "]");
          }
        }
        next();
      });
    },
  };
}
