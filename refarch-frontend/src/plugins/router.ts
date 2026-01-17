// Composables
import { createRouter, createWebHistory } from "vue-router";

import { getUser } from "@/api/user-client";
import { useRoleCheck } from "@/composables/useRoleCheck";
import { ROUTES_ADMIN, ROUTES_GETSTARTED, ROUTES_HOME } from "@/constants";
import { useUserStore } from "@/stores/user";
import User, { UserLocalDevelopment } from "@/types/User";
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
router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresWriterRole) {
    const userStore = useUserStore();
    let user = userStore.getUser;

    // Wait for user to be loaded if not yet available
    if (user === null) {
      try {
        // Load user data before allowing navigation
        user = await getUser();
        userStore.setUser(user);
      } catch {
        // No user info received, so fallback
        if (import.meta.env.DEV) {
          user = UserLocalDevelopment();
          userStore.setUser(user);
        } else {
          // No user available, redirect to homepage
          next({ name: ROUTES_HOME });
          return;
        }
      }
    }

    // Check if user has writer role using the composable
    const { hasRole } = useRoleCheck();
    const hasWriterRole = hasRole("writer").value;

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
