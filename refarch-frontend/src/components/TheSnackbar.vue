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
import {SNACKBAR_COLORS, SNACKBAR_DEFAULT_TIMEOUT} from "@/Constants";

const snackbarStore = useSnackbarStore();

const show = ref(false);
const timeout = ref(SNACKBAR_DEFAULT_TIMEOUT);
const message = ref("");
const color = ref(SNACKBAR_COLORS.INFO);

const isError = computed(() => color.value === SNACKBAR_COLORS.ERROR);

watch(
  () => snackbarStore.message,
  () => (message.value = snackbarStore.message ?? "")
);

watch(
  () => snackbarStore.level,
  () => {
    color.value = snackbarStore.level;
    if (color.value === SNACKBAR_COLORS.ERROR) {
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
