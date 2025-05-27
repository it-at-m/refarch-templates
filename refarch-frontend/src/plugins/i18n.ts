import { createI18n } from "vue-i18n";
import { de as deVuetify } from "vuetify/locale";

import deApp from "@/locales/de.json";

type MessageSchema = typeof deApp;
type Locales = "de";

const messages = {
  de: {
    $vuetify: {
      ...deVuetify,
    },
    ...deApp,
  },
};

const datetimeFormats = {
  de: {
    // TT.MM.JJJJ
    short: {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    },
    // TT.MM.JJJJ HH24:MM
    long: {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    },
    // TT.MM.JJJJ HH24:MM:SS
    timestamp: {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
    },
  },
};

export default createI18n<[MessageSchema], Locales>({
  legacy: false,
  locale: "de",
  fallbackLocale: "de",
  messages,
  // @ts-expect-error false positive for type mismatch (no tsc compilation error)
  datetimeFormats,
});
