<template>
  <v-snackbar-queue
    v-model="snackbarStore.queue"
    :timeout="5000"
    :color="STATUS_INDICATORS.INFO"
    closable
  >
    <template #text="textData">
      <v-layout class="align-center">
        <v-icon
          class="mr-3"
          size="25"
          :icon="textData.item.icon"
        />
        <p class="my-0 text-body-large">{{ textData.item.text }}</p>
      </v-layout>
    </template>
  </v-snackbar-queue>
</template>

<script setup lang="ts">
import { STATUS_INDICATORS } from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar";

const snackbarStore = useSnackbarStore();
</script>
