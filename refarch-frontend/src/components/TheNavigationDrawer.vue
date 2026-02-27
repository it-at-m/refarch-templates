<template>
  <v-navigation-drawer :model-value="showDrawer">
    <v-list>
      <v-list-item to="/getstarted">
        <v-list-item-title>
          {{ t("views.getStarted.navText") }}
        </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";

defineProps<{
  showDrawer: boolean;
}>();

const { t } = useI18n();
</script>
