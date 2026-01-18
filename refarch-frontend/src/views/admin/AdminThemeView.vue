<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <h1
          class="text-h3 font-weight-bold mb-3"
          style="color: #333333"
        >
          {{ t("views.admin.theme.title") }}
        </h1>
        <p
          class="text-body-1 mb-6"
          style="color: #333333"
        >
          {{ t("views.admin.theme.description") }}
        </p>
        <v-card
          class="pa-6"
          style="background-color: #ffffff; border: 1px solid #333333"
        >
          <v-card-title style="color: #333333">
            {{ t("views.admin.theme.cardTitle") }}
          </v-card-title>
          <v-card-text style="color: #333333">
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
</style>
