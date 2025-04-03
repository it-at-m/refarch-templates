import jsEslintConfig from "@eslint/js";
import vuePrettierEslintConfigSkipFormatting from "@vue/eslint-config-prettier/skip-formatting";
import {
  defineConfigWithVueTs,
  vueTsConfigs,
} from "@vue/eslint-config-typescript";
import { ESLint } from "eslint";
import vueEslintConfig from "eslint-plugin-vue";
import { globalIgnores } from "eslint/config";

export default defineConfigWithVueTs(
  ESLint.defaultConfig,
  jsEslintConfig.configs.recommended,
  vueEslintConfig.configs["flat/essential"],
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
  globalIgnores(["dist", "target", "node_modules", "env.d.ts"])
);
