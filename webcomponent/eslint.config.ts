import { fileURLToPath, URL } from "node:url";

import jsEslintConfig from "@eslint/js";
import {
  defineConfigWithVueTs,
  vueTsConfigs,
} from "@vue/eslint-config-typescript";
import { ESLint } from "eslint";
import skipFormatting from "eslint-config-prettier/flat";
import vueEslintConfig from "eslint-plugin-vue";
import { includeIgnoreFile } from "eslint/config";

import requireDataTestConfig from "./eslint-require-data-test-plugin";

const prettierIgnorePath = fileURLToPath(
  new URL(".prettierignore", import.meta.url)
);

export default defineConfigWithVueTs(
  ESLint.defaultConfig,
  jsEslintConfig.configs.recommended,
  vueEslintConfig.configs["flat/recommended-error"],
  vueTsConfigs.strict,
  vueTsConfigs.stylistic,
  requireDataTestConfig.configs["flat/muc-patternlab-vue"],
  skipFormatting,
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
      "vue/html-self-closing": [
        "error",
        {
          html: {
            void: "any",
          },
        },
      ],
    },
  },
  includeIgnoreFile(prettierIgnorePath, {
    gitignoreResolution: true,
  })
);
