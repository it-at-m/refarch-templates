import {
  mdiAlertCircleOutline,
  mdiAlertOutline,
  mdiCheckCircleOutline,
  mdiInformationOutline,
} from "@mdi/js";
import { defineStore } from "pinia";
import { ref } from "vue";

export interface SnackbarMessage {
  message: string | undefined;
  timeout?: number;
  level?: Levels;
  icon?: string;
}

export const enum Levels {
  SUCCESS = "success",
  INFO = "info",
  WARNING = "warning",
  ERROR = "error",
}

interface SnackbarInput {
  text: string | undefined;
  timeout: number;
  color: Levels;
  icon: string;
}

const DEFAULTS: Record<Levels, Pick<SnackbarInput, "timeout" | "icon">> = {
  [Levels.INFO]: { timeout: 2500, icon: mdiInformationOutline },
  [Levels.SUCCESS]: { timeout: 2500, icon: mdiCheckCircleOutline },
  [Levels.WARNING]: { timeout: 3500, icon: mdiAlertOutline },
  [Levels.ERROR]: { timeout: 4000, icon: mdiAlertCircleOutline },
} as const;

const normalizeSnackbar = (input: SnackbarMessage): SnackbarInput => {
  const color = input.level ?? Levels.INFO;
  const defaults = DEFAULTS[color];

  return {
    text: input.message,
    color,
    icon: input.icon ?? defaults.icon,
    timeout: input.timeout ?? defaults.timeout,
  };
};

/**
 * Store for messages which should be displayed by the snackbar
 */
export const useSnackbarStore = defineStore("snackbar", () => {
  const queue = ref<SnackbarInput[]>([]);

  /**
   * Adds default values if necessary
   */
  function add(message: SnackbarMessage) {
    queue.value.push(normalizeSnackbar(message));
  }

  return { queue, add };
});
