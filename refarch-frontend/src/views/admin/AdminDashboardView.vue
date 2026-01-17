<template>
  <v-container>
    <v-row class="text-center">
      <v-col cols="12">
        <h1
          class="text-h3 font-weight-bold mb-3"
          style="color: #333333"
        >
          CMS Admin Dashboard
        </h1>
        <p
          class="text-body-1"
          style="color: #333333"
        >
          Welcome to the Content Management System administration panel.
        </p>
        <v-progress-circular
          v-if="isLoading"
          indeterminate
          color="primary"
          class="mt-4"
        />
        <v-alert
          v-else-if="adminAccessGranted === true"
          type="success"
          class="mt-4"
        >
          {{ t("views.admin.statusGranted") }}
        </v-alert>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import { getAdminStatus } from "@/api/admin-client";
import { useRoleCheck } from "@/composables/useRoleCheck";
import { ROUTES_HOME } from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar";

const { t } = useI18n();
const router = useRouter();
const snackbarStore = useSnackbarStore();
const { hasWriterRole } = useRoleCheck();
const adminAccessGranted = ref<boolean | null>(null);
const isLoading = ref(false);

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
    .then((granted: boolean) => {
      adminAccessGranted.value = granted;
    })
    .catch(() => {
      // Error loading admin status - show error message to user
      snackbarStore.showMessage({
        message:
          "Fehler beim Laden des Admin-Status. Bitte versuchen Sie es erneut.",
      });
    })
    .finally(() => {
      isLoading.value = false;
    });
}
</script>

<style scoped>
/* Admin dashboard styles - black and white theme */
</style>
