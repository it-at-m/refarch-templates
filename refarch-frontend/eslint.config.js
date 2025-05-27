import jsEslintConfig from "@eslint/js";
import vueI18nEslintConfig from "@intlify/eslint-plugin-vue-i18n";
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
  vueI18nEslintConfig.configs.recommended,
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
      // Enforce i18n best practices manually as no stylistic ruleset exists yet
      "@intlify/vue-i18n/key-format-style": ["error"], // enforce camelCase for message keys
      "@intlify/vue-i18n/no-duplicate-keys-in-locale": ["error"],
      "@intlify/vue-i18n/no-missing-keys-in-other-locales": ["error"],
      "@intlify/vue-i18n/no-unknown-locale": ["error"],
      "@intlify/vue-i18n/no-unused-keys": ["error"],
    },
    settings: {
      "vue-i18n": {
        localeDir: "./src/locales/*.json",
        messageSyntaxVersion: "^11.0.0",
      },
    },
  },
  globalIgnores(["dist", "target", "node_modules", "env.d.ts"])
);
