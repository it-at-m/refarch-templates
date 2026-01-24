<template>
  <v-container>
    <v-row class="text-center mb-6">
      <v-col cols="12">
        <h1 class="text-h3 font-weight-bold mb-3 admin-text">
          {{ t("views.admin.dashboardTitle") }}
        </h1>
        <p class="text-body-1 admin-text">
          {{ t("views.admin.welcomeMessage") }}
        </p>
        <v-progress-circular
          v-if="isLoading"
          indeterminate
          color="primary"
          class="mt-4"
        />
        <v-alert
          v-else-if="adminStatus?.granted === true"
          type="success"
          class="mt-4"
        >
          {{ t("views.admin.statusGranted") }}
        </v-alert>
        <v-alert
          v-else-if="adminStatus?.granted === false"
          type="error"
          class="mt-4"
        >
          {{ t("views.admin.statusDenied") }}
        </v-alert>
      </v-col>
    </v-row>
    <v-row>
      <v-col
        cols="12"
        md="4"
      >
        <v-card
          :to="{ name: ROUTES_ADMIN_SETTINGS }"
          class="pa-6 text-center admin-card admin-card-link"
          hover
        >
          <v-icon
            size="64"
            class="admin-text"
          >
            {{ mdiCog }}
          </v-icon>
          <v-card-title class="mt-4 admin-text">
            {{ t("views.admin.dashboard.settingsCard.title") }}
          </v-card-title>
          <v-card-text class="admin-text">
            {{ t("views.admin.dashboard.settingsCard.description") }}
          </v-card-text>
        </v-card>
      </v-col>
      <v-col
        cols="12"
        md="4"
      >
        <v-card
          :to="{ name: ROUTES_ADMIN_THEME }"
          class="pa-6 text-center admin-card admin-card-link"
          hover
        >
          <v-icon
            size="64"
            class="admin-text"
          >
            {{ mdiPalette }}
          </v-icon>
          <v-card-title class="mt-4 admin-text">
            {{ t("views.admin.dashboard.themeCard.title") }}
          </v-card-title>
          <v-card-text class="admin-text">
            {{ t("views.admin.dashboard.themeCard.description") }}
          </v-card-text>
        </v-card>
      </v-col>
      <v-col
        cols="12"
        md="4"
      >
        <v-card class="pa-6 text-center admin-card">
          <v-icon
            size="64"
            class="admin-text"
          >
            {{ mdiPlus }}
          </v-icon>
          <v-card-title class="mt-4 admin-text">
            {{ t("views.admin.dashboard.placeholderCard.title") }}
          </v-card-title>
          <v-card-text class="admin-text">
            {{ t("views.admin.dashboard.placeholderCard.description") }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import type { AdminStatusResponse } from "@/api/admin-client";

import { mdiCog, mdiPalette, mdiPlus } from "@mdi/js";
import { onMounted, onUnmounted, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import { getAdminStatus } from "@/api/admin-client";
import { useRoleCheck } from "@/composables/useRoleCheck";
import {
  ROUTES_ADMIN_SETTINGS,
  ROUTES_ADMIN_THEME,
  ROUTES_HOME,
} from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar";

const { t } = useI18n();
const router = useRouter();
const snackbarStore = useSnackbarStore();
const { hasWriterRole } = useRoleCheck();
const adminStatus = ref<AdminStatusResponse | null>(null);
const isLoading = ref(false);
const deniedRedirectTimeout = ref<ReturnType<typeof setTimeout> | null>(null);

onMounted(() => {
  // Router guard already ensures user has writer role, but double-check for safety
  if (!hasWriterRole.value) {
    router.push({ name: ROUTES_HOME });
    return;
  }

  // Load admin status from backend
  loadAdminStatus();
});

function loadAdminStatus(): void {
  isLoading.value = true;
  getAdminStatus()
    .then((status: AdminStatusResponse) => {
      adminStatus.value = status;
      // If access is denied, redirect to home page after a short delay
      if (status.granted === false) {
        deniedRedirectTimeout.value = setTimeout(() => {
          router.push({ name: ROUTES_HOME });
        }, 3000);
      }
    })
    .catch(() => {
      // Error loading admin status - show error message to user
      snackbarStore.showMessage({
        message: t("views.admin.errorLoadingStatus"),
      });
    })
    .finally(() => {
      isLoading.value = false;
    });
}

onUnmounted(() => {
  if (deniedRedirectTimeout.value) {
    clearTimeout(deniedRedirectTimeout.value);
  }
});
</script>
