<template>
  <v-app-bar class="admin-app-bar">
    <v-row align="center">
      <v-col
        cols="3"
        class="d-flex align-center justify-start"
      >
        <v-app-bar-nav-icon
          class="admin-icon"
          @click.stop="toggleDrawer()"
        />
        <router-link :to="{ name: ROUTES_ADMIN }">
          <v-toolbar-title class="font-weight-bold admin-text-white">
            {{ t("views.admin.headerTitle") }}
          </v-toolbar-title>
        </router-link>
      </v-col>
      <v-col
        cols="9"
        class="d-flex align-center justify-end"
      >
        <v-btn
          v-if="userStore.getUser !== null"
          variant="text"
          icon
          class="admin-icon"
        >
          <ad2-image-avatar :username="userStore.getUser.username" />
        </v-btn>
      </v-col>
    </v-row>
  </v-app-bar>
  <v-navigation-drawer
    v-model="drawer"
    class="admin-drawer"
  >
    <v-list>
      <v-list-item :to="{ name: ROUTES_ADMIN }">
        <template #prepend>
          <v-icon class="admin-text">{{ mdiViewDashboard }}</v-icon>
        </template>
        <v-list-item-title>
          {{ t("views.admin.dashboardTitle") }}
        </v-list-item-title>
      </v-list-item>
      <v-list-item :to="{ name: ROUTES_ADMIN_SETTINGS }">
        <template #prepend>
          <v-icon class="admin-text">{{ mdiCog }}</v-icon>
        </template>
        <v-list-item-title>
          {{ t("views.admin.settings.title") }}
        </v-list-item-title>
      </v-list-item>
      <v-list-item :to="{ name: ROUTES_ADMIN_THEME }">
        <template #prepend>
          <v-icon class="admin-text">{{ mdiPalette }}</v-icon>
        </template>
        <v-list-item-title>
          {{ t("views.admin.theme.title") }}
        </v-list-item-title>
      </v-list-item>
      <v-divider class="my-2" />
      <v-list-item :to="{ name: ROUTES_HOME }">
        <template #prepend>
          <v-icon class="admin-text">{{ mdiHome }}</v-icon>
        </template>
        <v-list-item-title>
          {{ t("views.admin.homepageLink") }}
        </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import { mdiCog, mdiHome, mdiPalette, mdiViewDashboard } from "@mdi/js";
import { useToggle } from "@vueuse/core";
import { useI18n } from "vue-i18n";

import Ad2ImageAvatar from "@/components/common/Ad2ImageAvatar.vue";
import {
  ROUTES_ADMIN,
  ROUTES_ADMIN_SETTINGS,
  ROUTES_ADMIN_THEME,
  ROUTES_HOME,
} from "@/constants";
import { useUserStore } from "@/stores/user";

const { t } = useI18n();

const userStore = useUserStore();
const [drawer, toggleDrawer] = useToggle();
</script>
