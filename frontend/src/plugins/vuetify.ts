import "vuetify/styles";

import type { VueI18nAdapterParams } from "vuetify/locale/adapters/vue-i18n";

import { useI18n } from "vue-i18n";
import { createVuetify } from "vuetify";
import { VuetifyDateAdapter } from "vuetify/date/adapters/vuetify";
import { aliases, mdi } from "vuetify/iconsets/mdi-svg";
import { createVueI18nAdapter } from "vuetify/locale/adapters/vue-i18n";

import i18n from "@/plugins/i18n";

export default createVuetify({
  /**
   * Configure global default properties for components via the `defaults` object ({@link https://vuetifyjs.com/en/features/global-configuration/#contextual-defaults[See more here]})
   */
  icons: {
    defaultSet: "mdi",
    aliases,
    sets: {
      mdi,
    },
  },
  theme: {
    themes: {
      light: {
        colors: {
          primary: "#333333",
          secondary: "#FFCC00",
          accent: "#7BA4D9",
          success: "#69BE28",
          error: "#FF0000",
        },
      },
      dark: {
        colors: {
          primary: "#333333",
          secondary: "#FFCC00",
          accent: "#90CAF9",
          success: "#81C784",
          error: "#E57373",
        },
      },
    },
  },
  locale: {
    // @ts-expect-error false positive for type mismatch (no tsc compilation error)
    adapter: createVueI18nAdapter({ i18n, useI18n } as VueI18nAdapterParams),
  },
  date: {
    adapter: VuetifyDateAdapter,
    locale: {
      de: "de-DE",
    },
  },
});
