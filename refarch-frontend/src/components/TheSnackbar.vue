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
import { VBtn, VSnackbar } from "vuetify/components";

import { useSnackbarStore } from "@/stores/snackbar";

const snackbarStore = useSnackbarStore();

const defaultTimeout = 5000;

const show = ref(false);
const timeout = ref(defaultTimeout);
const message = ref("");
const color = ref("info");

const isError = computed(() => color.value === "error");

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

function hide(): void {
  show.value = false;
}
</script>
