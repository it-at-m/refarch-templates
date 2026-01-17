// Composables
import { createRouter, createWebHistory } from "vue-router";

import { ROUTES_ADMIN, ROUTES_GETSTARTED, ROUTES_HOME } from "@/constants";
import { useUserStore } from "@/stores/user";
import AdminDashboardView from "@/views/admin/AdminDashboardView.vue";
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
  {
    path: "/admin",
    name: ROUTES_ADMIN,
    component: AdminDashboardView,
    meta: {
      requiresWriterRole: true,
    },
  },
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

// Navigation guard to check writer role for admin routes
router.beforeEach((to, from, next) => {
  if (to.meta.requiresWriterRole) {
    const user = useUserStore().getUser;

    // Wait for user to be loaded if not yet available
    if (user === null) {
      // User not loaded yet, allow navigation but check in component
      next();
      return;
    }

    // Check if user has writer role
    const hasWriterRole =
      user.user_roles?.includes("writer") ||
      user.authorities?.some(
        (auth) => auth === "writer" || auth === "ROLE_writer"
      );

    if (hasWriterRole) {
      next();
    } else {
      // Redirect to homepage if user doesn't have writer role
      next({ name: ROUTES_HOME });
    }
  } else {
    next();
  }
});

export default router;
