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
    <template #actions="{ props, item }">
      <v-btn
        v-if="item.color == 'error'"
        color="primary"
        variant="text"
        v-bind="props"
        >{{ t("common.actions.close") }}</v-btn
      >
    </template>
  </v-snackbar-queue>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";

import { useSnackbarStore } from "@/stores/snackbar.ts";

const snackbareStore = useSnackbarStore();

const { t } = useI18n();
</script>
