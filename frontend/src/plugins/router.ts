import { createRouter, createWebHistory } from "vue-router";
import { routes as fileBasedRoutes, handleHotUpdate } from "vue-router/auto-routes";



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

  if (!to.meta.requiredRoles) {
    return true;
  }

  const requiredRoles = Array.isArray(to.meta.requiredRoles)
    ? to.meta.requiredRoles
    : [to.meta.requiredRoles];

  const userRoles =
    (userInfoStore.userInfo?.resource_access?.local?.roles as string[]) || [];

  return requiredRoles.some((role) => userRoles.includes(role));
});

if (import.meta.hot) {
  handleHotUpdate(router);
}

export default router;
