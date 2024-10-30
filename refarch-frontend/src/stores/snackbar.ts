import { defineStore } from "pinia";
import { ref } from "vue";

import { STATUS_INDICATORS } from "@/constants";

export interface SnackbarState {
  message: string | undefined;
  level: STATUS_INDICATORS;
  show: boolean;
}

export const useSnackbarStore = defineStore("snackbar", () => {
  const message = ref<string | undefined>(undefined);
  const level = ref(STATUS_INDICATORS.INFO);
  const show = ref(false);
  function showMessage(messageI: {
    message?: string;
    level?: STATUS_INDICATORS;
    show?: boolean;
  }): void {
    message.value = messageI.message;
    level.value = messageI.level ? messageI.level : STATUS_INDICATORS.INFO;
    show.value = true;
  }
  function updateShow(showI: boolean): void {
    show.value = showI;
  }
  return { message, level, show, showMessage, updateShow };
});
