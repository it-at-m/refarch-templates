import type { UserInfo } from "@/types/UserInfo";

import { defineStore } from "pinia";
import { readonly, ref } from "vue";

import { getUserInfo } from "@/api/userinfo-client";
import { STATUS_INDICATORS } from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar";

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

  return { userInfo, fetchUserInfo };
});
