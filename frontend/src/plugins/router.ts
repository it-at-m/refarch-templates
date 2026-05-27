import { mdiShieldLock } from "@mdi/js";
import { createRouter, createWebHistory } from "vue-router";
import {
  routes as fileBasedRoutes,
  handleHotUpdate,
} from "vue-router/auto-routes";

import { hasAnyRole } from "@/composables/useHasAnyRole";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";
import { useUserInfoStore } from "@/stores/userinfo";

const routes = [
  ...fileBasedRoutes,
  { path: "/:catchAll(.*)*", redirect: "/" }, // CatchAll route
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return {
      top: 0,
      left: 0,
    };
  },
});

router.beforeEach(async (to, from) => {
  const userInfoStore = useUserInfoStore();
  const snackbarStore = useSnackbarStore();
  if (!userInfoStore.userInfo) {
    await userInfoStore.fetchUserInfo();
  }

  if (
    !to.meta.hasAnyRole ||
    hasAnyRole(to.meta.hasAnyRole, userInfoStore.currentRoles)
  ) {
    return true;
  }

  snackbarStore.push({
    color: STATUS_INDICATORS.ERROR,
    text: "Du hast nicht die erforderlichen Berechtigungen, um diese Seite anzuzeigen.",
    icon: mdiShieldLock,
  });

  // Check if application was already running in browser
  if (from.name) {
    return false;
  }

  return { path: "/" };
});

if (import.meta.hot) {
  handleHotUpdate(router);
}

export default router;
