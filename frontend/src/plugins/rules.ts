import type { Plugin } from "vue";

import { createRulesPlugin } from "vuetify";

import vuetify from "@/plugins/vuetify";
import { maxRule, minRule, uniqueRule } from "@/util/rules";

/**
 * Plugin that registers Vuetify-provided rules (see https://v4.vuetifyjs.com/en/features/rules/#api) and custom validation rules for use with
 * the `:rules` prop of Vuetify form components.
 *
 * Registered rules are available through the `useRules` composable, e.g.:
 * - `rules.maxLength(5)` for a Vuetify provided rule
 * - `rules['max']!(5, false)`
 *
 * More information in https://vuetifyjs.com/en/features/rules/
 */
export default createRulesPlugin(
  {
    aliases: {
      min: minRule,
      max: maxRule,
      unique: uniqueRule,
    },
  },
  vuetify.locale
) as Plugin;
