import { createRouter, createWebHistory } from "vue-router";
import {
  routes as fileBasedRoutes,
  handleHotUpdate,
} from "vue-router/auto-routes";

import { hasAnyRole } from "@/composables/useHasAnyRole";
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

const userInfoStore = useUserInfoStore();
router.beforeEach(async (to, from) => {
  if (!userInfoStore.userInfo) {
    await userInfoStore.fetchUserInfo();
  }

  if (
    !to.meta.hasAnyRole ||
    hasAnyRole(to.meta.hasAnyRole, userInfoStore.currentRoles)
  ) {
    return true;
  }

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
