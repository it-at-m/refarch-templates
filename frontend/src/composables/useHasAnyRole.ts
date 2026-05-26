import type { Role } from "@/types/Role";

import { computed } from "vue";

import { useUserInfoStore } from "@/stores/userinfo";

export default function useHasAnyRole(roles: Role | Role[]) {
  const userInfoStore = useUserInfoStore();
  return computed(() => hasAnyRole(roles, userInfoStore.currentRoles));
}

/**
 * Helper function: Only used in route guard, do not use directly
 * @param roles
 * @param currentRoles
 */
export function hasAnyRole(roles: Role | Role[], currentRoles: Role[]) {
  const requiredRoles = Array.isArray(roles) ? roles : [roles];
  return requiredRoles.some((requiredRole) =>
    currentRoles.includes(requiredRole)
  );
}
