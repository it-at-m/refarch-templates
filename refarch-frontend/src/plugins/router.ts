// Composables
import { createRouter, createWebHistory } from "vue-router";

import { getConfig } from "@/api/fetch-utils";
import { useRoleCheck } from "@/composables/useRoleCheck";
import {
  ROUTES_ADMIN,
  ROUTES_ADMIN_SETTINGS,
  ROUTES_ADMIN_THEME,
  ROUTES_GETSTARTED,
  ROUTES_HOME,
} from "@/constants";
import { useUserStore } from "@/stores/user";
import User from "@/types/User";
import AdminDashboardView from "@/views/admin/AdminDashboardView.vue";
import AdminSettingsView from "@/views/admin/AdminSettingsView.vue";
import AdminThemeView from "@/views/admin/AdminThemeView.vue";
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
  {
    path: "/admin/settings",
    name: ROUTES_ADMIN_SETTINGS,
    component: AdminSettingsView,
    meta: {
      requiresWriterRole: true,
    },
  },
  {
    path: "/admin/theme",
    name: ROUTES_ADMIN_THEME,
    component: AdminThemeView,
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
        // Try to load user data - check response status to detect unauthenticated users
        const response = await fetch("api/sso/userinfo", getConfig());

        // If 401 Unauthorized or 403 Forbidden, user is not logged in - redirect immediately
        if (response.status === 401 || response.status === 403) {
          next({ name: ROUTES_HOME });
          return;
        }

        // If response is not ok, user is not authenticated
        if (!response.ok) {
          next({ name: ROUTES_HOME });
          return;
        }

        // User is authenticated, parse and store user data
        const json: Partial<User> = await response.json();
        const u = new User();
        u.sub = json.sub || "";
        u.displayName = json.displayName || "";
        u.surname = json.surname || "";
        u.telephoneNumber = json.telephoneNumber || "";
        u.email = json.email || "";
        u.username = json.username || "";
        u.givenname = json.givenname || "";
        u.department = json.department || "";
        u.lhmObjectID = json.lhmObjectID || "";
        u.preferred_username = json.preferred_username || "";
        u.memberof = json.memberof || [];
        u.user_roles = json.user_roles || [];
        u.authorities = json.authorities || [];
        user = u;
        userStore.setUser(user);
      } catch {
        // Error loading user - redirect to homepage
        // Don't use UserLocalDevelopment() fallback in router guard
        // This ensures admin routes are only accessible to authenticated users
        next({ name: ROUTES_HOME });
        return;
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
