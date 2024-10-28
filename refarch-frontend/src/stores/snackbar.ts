import { defineStore } from "pinia";
import { ref } from "vue";

import { Levels } from "@/api/ApiError";

export interface SnackbarState {
  message: string | undefined;
  level: Levels;
  show: boolean;
}

export const useSnackbarStore = defineStore("snackbar", () => {
  const message = ref<string | undefined>(undefined);
  const level = ref(Levels.INFO);
  const show = ref(false);
  function showMessage(messageI: {
    message?: string;
    level?: Levels;
    show?: boolean;
  }): void {
    message.value = messageI.message;
    level.value = messageI.level ? messageI.level : Levels.INFO;
    show.value = true;
  }
  function updateShow(showI: boolean): void {
    show.value = showI;
  }
  return { message, level, show, showMessage, updateShow };
});
