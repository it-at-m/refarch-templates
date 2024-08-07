import type { App } from "vue";

import pinia from "@/plugins/pinia";
import router from "@/plugins/router";
import vuetify from "@/plugins/vuetify";

export function registerPlugins(app: App) {
  app.use(vuetify).use(router).use(pinia);
}
