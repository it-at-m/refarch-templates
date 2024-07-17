import { defineStore } from "pinia";
import { computed, ref } from "vue";

import User from "@/types/User";

export interface UserState {
  user: User | null;
}

export const useUserStore = defineStore("user", () => {
  const user = ref<User | null>(null);

  const getUser = computed((): User | null => {
    return user.value;
  });

  function setUser(payload: User | null): void {
    user.value = payload;
  }
  return { getUser, setUser };
});
