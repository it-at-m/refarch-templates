import { fileURLToPath, URL } from "node:url";

import jsEslintConfig from "@eslint/js";
import vuePrettierEslintConfigSkipFormatting from "@vue/eslint-config-prettier/skip-formatting";
import {
  defineConfigWithVueTs,
  vueTsConfigs,
} from "@vue/eslint-config-typescript";
import { ESLint } from "eslint";
import vueEslintConfig from "eslint-plugin-vue";
import { includeIgnoreFile } from "eslint/config";

const prettierIgnorePath = fileURLToPath(
  new URL(".prettierignore", import.meta.url)
);

export default defineConfigWithVueTs(
  ESLint.defaultConfig,
  jsEslintConfig.configs.recommended,
  vueEslintConfig.configs["flat/recommended-error"],
  vueTsConfigs.strict,
  vueTsConfigs.stylistic,
  vuePrettierEslintConfigSkipFormatting,
  {
    linterOptions: {
      reportUnusedDisableDirectives: "error",
      reportUnusedInlineConfigs: "error",
    },
    rules: {
      "no-console": ["error", { allow: ["debug"] }],
      "vue/component-name-in-template-casing": [
        "error",
        "kebab-case",
        { registeredComponentsOnly: false },
      ],
    },
  },
  includeIgnoreFile(prettierIgnorePath, {
    gitignoreResolution: true,
  })
);
