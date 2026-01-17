import { computed, type ComputedRef } from "vue";

import { useUserStore } from "@/stores/user";

/**
 * Composable for checking user roles
 */
export function useRoleCheck() {
  const userStore = useUserStore();

  /**
   * Checks if the current user has the writer role
   */
  const hasWriterRole = computed((): boolean => {
    const user = userStore.getUser;
    if (!user) {
      return false;
    }
    // Check both user_roles and authorities arrays
    // user_roles contains raw role names like "writer"
    // authorities may contain "ROLE_writer" format
    return (
      user.user_roles?.includes("writer") ||
      user.authorities?.some(
        (auth) => auth === "writer" || auth === "ROLE_writer"
      )
    );
  });

  /**
   * Checks if the current user has a specific role.
   * Returns a computed ref that automatically updates when the user changes.
   *
   * @param roleName - The role name to check (e.g., "writer", "reader")
   * @returns A computed ref that is true if the user has the role, false otherwise
   */
  function hasRole(roleName: string): ComputedRef<boolean> {
    return computed(() => {
      const user = userStore.getUser;
      if (!user) {
        return false;
      }
      return (
        user.user_roles?.includes(roleName) ||
        user.authorities?.some(
          (auth) => auth === roleName || auth === `ROLE_${roleName}`
        )
      );
    });
  }

  return {
    hasWriterRole,
    hasRole,
  };
}
