import type { I18nOptions } from "vue-i18n";

/*
 * All i18n resources specified in the plugin `include` option can be loaded
 * at once using the import syntax
 */
import messages from "@intlify/unplugin-vue-i18n/messages";
import { createI18n } from "vue-i18n";

const datetimeFormats: I18nOptions["datetimeFormats"] = {
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

export default createI18n({
  legacy: false,
  locale: "de",
  fallbackLocale: "de",
  messages,
  datetimeFormats,
});
