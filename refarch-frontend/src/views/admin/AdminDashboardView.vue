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
        <v-alert
          v-if="adminStatus"
          type="success"
          class="mt-4"
        >
          {{ adminStatus.message }}
        </v-alert>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import type { AdminStatusResponse } from "@/api/admin-client";

import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";

import { getAdminStatus } from "@/api/admin-client";
import { useRoleCheck } from "@/composables/useRoleCheck";
import { ROUTES_HOME } from "@/constants";

const router = useRouter();
const { hasWriterRole } = useRoleCheck();
const adminStatus = ref<AdminStatusResponse | null>(null);

onMounted(() => {
  // Check if user has writer role, redirect if not
  if (!hasWriterRole.value) {
    router.push({ name: ROUTES_HOME });
    return;
  }

  // Load admin status from backend
  loadAdminStatus();
});

function loadAdminStatus(): void {
  getAdminStatus()
    .then((status: AdminStatusResponse) => {
      adminStatus.value = status;
    })
    .catch(() => {
      // Error loading admin status - user might not have access
      if (!hasWriterRole.value) {
        router.push({ name: ROUTES_HOME });
      }
    });
}
</script>

<style scoped>
/* Admin dashboard styles - black and white theme */
</style>
