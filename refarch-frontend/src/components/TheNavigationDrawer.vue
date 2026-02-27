<template>
  <v-navigation-drawer
    v-model="showDrawer"
    temporary
  >
    <v-list>
      <v-list-item to="/">
        <v-list-item-title>
          {{ t("views.index.navText") }}
        </v-list-item-title>
      </v-list-item>
      <v-list-item to="/documentation">
        <v-list-item-title>
          {{ t("views.documentation.navText") }}
        </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";

const showDrawer = defineModel<boolean>({ required: true });

const { t } = useI18n();
</script>
