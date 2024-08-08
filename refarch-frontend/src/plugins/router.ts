// Composables
import { createRouter, createWebHashHistory } from "vue-router";

import { ROUTES_GETSTARTED, ROUTES_HOME } from "@/Constants";
import GetStartedView from "@/views/GetStartedView.vue";
import HomeView from "@/views/HomeView.vue";

const routes = [
  {
    path: "/",
    name: ROUTES_HOME,
    component: HomeView,
    meta: {},
  },
  {
    path: "/getstarted",
    name: ROUTES_GETSTARTED,
    component: GetStartedView,
  },
  { path: "/:catchAll(.*)*", redirect: "/" }, // CatchAll route
];

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes,
  scrollBehavior() {
    return {
      top: 0,
      left: 0
    }
  }
});

export default router;
