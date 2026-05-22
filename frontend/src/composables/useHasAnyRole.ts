import type { Role } from "@/types/Role";

import { computed } from "vue";

import { useUserInfoStore } from "@/stores/userinfo";

export function useHasAnyRole(roles: Role | Role[]) {
  const { currentRoles } = useUserInfoStore();
  const requiredRoles = Array.isArray(roles) ? roles : [roles];
  return computed(() =>
    requiredRoles.some((requiredRole) => currentRoles.includes(requiredRole))
  );
}
