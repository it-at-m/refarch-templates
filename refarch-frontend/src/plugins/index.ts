import type { App } from "vue";

import i18n from "@/plugins/i18n";
import pinia from "@/plugins/pinia";
import router from "@/plugins/router";
import vuetify from "@/plugins/vuetify";

export function registerPlugins(app: App) {
  app.use(router).use(pinia).use(i18n).use(vuetify);
}
