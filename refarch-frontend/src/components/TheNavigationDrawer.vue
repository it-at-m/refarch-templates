<template>
  <v-navigation-drawer
    v-model="showDrawer"
    temporary
    :scrim="false"
  >
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

const showDrawer = defineModel<boolean>({ required: true });

const { t } = useI18n();
</script>
