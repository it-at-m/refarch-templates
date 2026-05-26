import type { UserInfo } from "@/types/UserInfo";

import { defineStore } from "pinia";
import { computed, readonly, ref } from "vue";

import { getUserInfo } from "@/api/userinfo-client";
import { STATUS_INDICATORS } from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar";
import { Role } from "@/types/Role";

function isRole(value: string): value is Role {
  return Object.values(Role).includes(value as Role);
}

export const useUserInfoStore = defineStore("userInfo", () => {
  const snackbarStore = useSnackbarStore();
  const internalUserInfo = ref<UserInfo | null>(null);
  const userInfo = readonly(internalUserInfo);

  async function fetchUserInfo(): Promise<void> {
    try {
      internalUserInfo.value = await getUserInfo();
    } catch {
      snackbarStore.push({
        color: STATUS_INDICATORS.ERROR,
        text: "Nutzer konnte nicht geladen werden.",
      });
    }
  }

  const currentRoles = computed(() => {
    const allUserInfoRoles =
      Object.values(internalUserInfo.value?.resource_access ?? {})[0]?.roles ??
      [];
    return allUserInfoRoles.filter(isRole);
  });

  return { userInfo, currentRoles, fetchUserInfo };
});
