<template>
  <v-snackbar
    id="snackbar"
    v-model="show"
    :color="color"
    :timeout="timeout"
  >
    {{ message }}
    <v-btn
      v-if="isError"
      color="primary"
      variant="text"
      @click="hide"
    >
      Schließen
    </v-btn>
  </v-snackbar>
</template>

<script setup lang="ts">
import { computed, ref, watch } from "vue";

import { SNACKBAR_DEFAULT_TIMEOUT, STATUS_INDICATORS } from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar";

const snackbarStore = useSnackbarStore();

const show = ref(false);
const timeout = ref(SNACKBAR_DEFAULT_TIMEOUT);
const message = ref("");
const color = ref(STATUS_INDICATORS.INFO);

const isError = computed(() => color.value === STATUS_INDICATORS.ERROR);

watch(
  () => snackbarStore.message,
  () => (message.value = snackbarStore.message ?? "")
);

watch(
  () => snackbarStore.level,
  () => {
    color.value = snackbarStore.level;
    if (color.value === STATUS_INDICATORS.ERROR) {
      timeout.value = -1;
    } else {
      timeout.value = SNACKBAR_DEFAULT_TIMEOUT;
    }
  }
);

watch(
  () => snackbarStore.show,
  () => {
    if (snackbarStore.show) {
      show.value = false;
      setTimeout(() => {
        show.value = true;
        snackbarStore.show = false;
      }, timeout.value);
    }
  }
);

function hide(): void {
  show.value = false;
}
</script>
