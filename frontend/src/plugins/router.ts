import { createRouter, createWebHistory } from "vue-router";
import { routes as fileBasedRoutes, handleHotUpdate } from "vue-router/auto-routes";



import { useHasAnyRole } from "@/composables/useHasAnyRole";
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

router.beforeEach(async (to) => {
  const userInfoStore = useUserInfoStore();
  if (!userInfoStore.userInfo) {
    await userInfoStore.fetchUserInfo();
  }

  if (!to.meta.hasAnyRole || useHasAnyRole(to.meta.hasAnyRole).value) {
    return;
  }

  return { path: "/" };
});

if (import.meta.hot) {
  handleHotUpdate(router);
}

export default router;
