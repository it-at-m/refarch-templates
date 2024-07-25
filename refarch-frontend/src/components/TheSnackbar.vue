<template>
  <v-snackbar
    id="snackbar"
    v-model="show"
    :color="color"
    :timeout="timeout"
  >
    {{ message }}
    <v-btn
      v-if="color === 'error'"
      color="primary"
      variant="text"
      @click="show = false"
    >
      Schließen
    </v-btn>
  </v-snackbar>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";

import { useSnackbarStore } from "@/stores/snackbar";

const snackbarStore = useSnackbarStore();

const defaultTimeout = 5000;

const show = ref(false);
const timeout = ref(defaultTimeout);
const message = ref("");
const color = ref("info");

watch(
  () => snackbarStore.message,
  () => (message.value = snackbarStore.message ?? "")
);

watch(
  () => snackbarStore.level,
  () => {
    color.value = snackbarStore.level;
    if (color.value === "error") {
      timeout.value = 0;
    } else {
      timeout.value = defaultTimeout;
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
      }, 100);
    }
  }
);
</script>
