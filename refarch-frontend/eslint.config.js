import js from "@eslint/js";
import vueI18n from "@intlify/eslint-plugin-vue-i18n";
import vuePrettierEslintConfigSkipFormatting from "@vue/eslint-config-prettier/skip-formatting";
import vueTsEslintConfig from "@vue/eslint-config-typescript";
import { ESLint } from "eslint";
import vueEslintConfig from "eslint-plugin-vue";

export default [
  ...ESLint.defaultConfig,
  js.configs.recommended,
  ...vueEslintConfig.configs["flat/recommended"],
  ...vueTsEslintConfig({
    extends: ["strict", "stylistic"],
  }),
  ...vueI18n.configs["flat/recommended"],
  vuePrettierEslintConfigSkipFormatting,
  {
    ignores: ["dist", "target", "node_modules", "env.d.ts"],
  },
  {
    rules: {
      "no-console": ["error", { allow: ["debug"] }],
      "vue/component-name-in-template-casing": [
        "error",
        "kebab-case",
        { registeredComponentsOnly: false },
      ],
      "@intlify/vue-i18n/no-raw-text": ["warn"], // encourage usage of i18n for static text
      "@intlify/vue-i18n/key-format-style": ["error", "camelCase"], // enforce camelCase for message keys
      "@intlify/vue-i18n/no-duplicate-keys-in-locale": ["error"],
      "@intlify/vue-i18n/no-unused-keys": [
        "error",
        {
          src: "./src",
          extensions: [".ts", ".vue"],
          ignores: [],
          enableFix: false,
        },
      ],
    },
    settings: {
      "vue-i18n": {
        localeDir: "./src/locales/*.yaml",
        messageSyntaxVersion: "^10.0.0",
      },
    },
  },
];
