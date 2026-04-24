import type { UserInfo } from "@/types/UserInfo";

import { defineStore } from "pinia";
import { computed, ref } from "vue";

export const useUserInfoStore = defineStore("userInfo", () => {
  const userInfo = ref<UserInfo | null>(null);

  const getUserInfo = computed((): UserInfo | null => {
    return userInfo.value;
  });

  function setUserInfo(payload: UserInfo | null): void {
    userInfo.value = payload;
  }

  return { getUserInfo, setUserInfo };
});
