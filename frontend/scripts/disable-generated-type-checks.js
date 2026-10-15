import { readdir, readFile, writeFile } from "node:fs/promises";
import { join } from "node:path";
import { fileURLToPath } from "node:url";

const generatedDirectory = fileURLToPath(
  new URL("../src/api/generated", import.meta.url)
);
const tsNoCheck = "// @ts-nocheck\n";

async function disableTypeChecks(directory) {
  const entries = await readdir(directory, { withFileTypes: true });

  await Promise.all(
    entries.map(async (entry) => {
      const path = join(directory, entry.name);

      if (entry.isDirectory()) {
        await disableTypeChecks(path);
        return;
      }

      if (!entry.isFile() || !entry.name.endsWith(".ts")) {
        return;
      }

      const content = await readFile(path, "utf8");
      if (content.startsWith(tsNoCheck)) {
        return;
      }

      await writeFile(path, `${tsNoCheck}${content}`);
    })
  );
}

await disableTypeChecks(generatedDirectory);
