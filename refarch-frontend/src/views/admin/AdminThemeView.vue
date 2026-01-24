<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <h1 class="text-h3 font-weight-bold mb-3 admin-text">
          {{ t("views.admin.theme.title") }}
        </h1>
        <p class="text-body-1 mb-6 admin-text">
          {{ t("views.admin.theme.description") }}
        </p>
        <v-card class="pa-6 admin-card">
          <v-card-title class="admin-text">
            {{ t("views.admin.theme.cardTitle") }}
          </v-card-title>
          <v-card-text class="admin-text">
            {{ t("views.admin.theme.cardContent") }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";

const { t } = useI18n();
</script>

<style scoped>
/* Admin theme page styles - black and white theme */
.admin-text {
  color: #333333;
}

.admin-card {
  background-color: #ffffff;
  border: 1px solid #333333;
}
</style>
