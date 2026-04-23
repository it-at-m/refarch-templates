import {
  mdiAlertCircleOutline,
  mdiAlertOutline,
  mdiCheckCircleOutline,
  mdiInformationOutline,
} from "@mdi/js";
import { defineStore } from "pinia";
import { ref } from "vue";

import { STATUS_INDICATORS } from "@/constants";

interface SnackbarMessage {
  text: string;
  timeout?: number;
  color?: STATUS_INDICATORS;
  icon?: string;
}

const DEFAULTS: Record<
  STATUS_INDICATORS,
  Pick<SnackbarMessage, "timeout" | "icon">
> = {
  [STATUS_INDICATORS.INFO]: { icon: mdiInformationOutline },
  [STATUS_INDICATORS.SUCCESS]: { icon: mdiCheckCircleOutline },
  [STATUS_INDICATORS.WARNING]: { icon: mdiAlertOutline },
  [STATUS_INDICATORS.ERROR]: { timeout: -1, icon: mdiAlertCircleOutline },
} as const;

/**
 * Applying defaults for SnackbarMessage by using the DEFAULTS-records.
 * Color is extracted from the enum STATUS_INDICATORS itself.
 * @param input the incoming message with possible missing values.
 */
function applyDefaults(input: SnackbarMessage): SnackbarMessage {
  const color = input.color ?? STATUS_INDICATORS.INFO;
  const defaults = DEFAULTS[color];

  return {
    text: input.text,
    color,
    icon: input.icon ?? defaults.icon,
    timeout: input.timeout ?? defaults.timeout,
  };
}

/**
 * Store for messages which should be displayed by the snackbar
 */
export const useSnackbarStore = defineStore("snackbar", () => {
  const queue = ref<SnackbarMessage[]>([]);

  /**
   * Adds default values if necessary
   */
  function push(message: SnackbarMessage) {
    queue.value.push(applyDefaults(message));
  }

  return { queue, push };
});
