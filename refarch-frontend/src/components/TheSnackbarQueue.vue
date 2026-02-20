<template>
  <v-snackbar-queue v-model="snackbareStore.queue">
    <template #text="textData">
      <v-layout class="align-center">
        <v-icon
          class="mr-3"
          size="25"
          :icon="textData.item.icon"
        />
        <p class="text-body-1">{{ textData.item.text }}</p>
      </v-layout>
    </template>
  </v-snackbar-queue>
</template>

<script setup lang="ts">
import { useSnackbarStore } from "@/stores/snackbar.ts";

const snackbareStore = useSnackbarStore();
</script>
