import type { Plugin } from "vite";

export default function EncodeBracketsPlugin(): Plugin {
    return {
        name: "encode-brackets",
        configureServer(server) {
                server.middlewares.use((req, res, next) => {
                    if (req.url) {
                        console.log(req.url)
                        req.url = req.url
                            .replace(/\[/g, "%5B")
                            .replace(/\]/g, "%5D");
                    }
                    next();
                });
        }
    }
}