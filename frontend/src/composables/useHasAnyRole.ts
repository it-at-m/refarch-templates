import type { Role } from "@/types/Role";

import { computed } from "vue";

import { useUserInfoStore } from "@/stores/userinfo";

export default function useHasAnyRole(roles: Role | Role[]) {
  const userInfoStore = useUserInfoStore();
  return computed(() => hasAnyRole(roles, userInfoStore.currentRoles));
}

/**
 * Helper function: Only use directly in route guard, use useHasAnyRole in components.
 * @param requiredRoles - A single role or an array of roles to check against the user's roles.
 * @param userRoles An array of roles that the user currently possesses.
 * @return true if the user has at least one of the required roles, otherwise false.
 */
export function hasAnyRole(requiredRoles: Role | Role[], userRoles: Role[]) {
  const requiredRolesArray = Array.isArray(requiredRoles)
    ? requiredRoles
    : [requiredRoles];
  return requiredRolesArray.some((requiredRole) =>
    userRoles.includes(requiredRole)
  );
}
