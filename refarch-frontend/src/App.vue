<template>
  <v-app>
    <the-snackbar />
    <admin-header v-if="isAdminRoute" />
    <v-app-bar v-else color="primary">
      <v-row align="center">
        <v-col
          cols="3"
          class="d-flex align-center justify-start"
        >
          <v-app-bar-nav-icon @click.stop="toggleDrawer()" />
          <router-link to="/">
            <v-toolbar-title class="font-weight-bold">
              <span class="text-white">{{ t("app.name.part1") }}</span>
              <span class="text-secondary">{{ t("app.name.part2") }}</span>
              <span class="text-white">{{ t("app.name.part3") }}</span>
            </v-toolbar-title>
          </router-link>
        </v-col>
        <v-col
          cols="6"
          class="d-flex align-center justify-center"
        >
          <v-text-field
            id="searchField"
            v-model="query"
            flat
            variant="solo-inverted"
            hide-details
            :label="t('app.search')"
            clearable
            :prepend-inner-icon="mdiMagnify"
            theme="dark"
            @keyup.enter="search"
          />
        </v-col>
        <v-col
          cols="3"
          class="d-flex align-center justify-end"
        >
          <app-switcher
            v-if="appswitcherBaseUrl"
            :base-url="appswitcherBaseUrl"
            :tags="['global']"
            :icon="mdiApps"
          />
          <v-btn
            variant="text"
            icon
          >
            <ad2-image-avatar
              v-if="userStore.getUser !== null"
              :username="userStore.getUser.username"
            />
          </v-btn>
        </v-col>
      </v-row>
    </v-app-bar>
    <v-navigation-drawer v-if="!isAdminRoute" v-model="drawer">
      <v-list>
        <v-list-item :to="{ name: ROUTES_GETSTARTED }">
          <v-list-item-title>
            {{ t("views.getStarted.navText") }}
          </v-list-item-title>
        </v-list-item>
        <v-list-item
          v-if="hasWriterRole"
          :to="{ name: ROUTES_ADMIN }"
        >
          <v-list-item-title>
            Admin Dashboard
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-main>
      <v-container fluid>
        <router-view v-slot="{ Component }">
          <v-fade-transition mode="out-in">
            <component :is="Component" />
          </v-fade-transition>
        </router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { mdiApps, mdiMagnify } from "@mdi/js";
import { AppSwitcher } from "@muenchen/appswitcher-vue";
import { useToggle } from "@vueuse/core";
import { computed, onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRoute } from "vue-router";

import { getUser } from "@/api/user-client";
import AdminHeader from "@/components/admin/AdminHeader.vue";
import Ad2ImageAvatar from "@/components/common/Ad2ImageAvatar.vue";
import TheSnackbar from "@/components/TheSnackbar.vue";
import { useRoleCheck } from "@/composables/useRoleCheck";
import { APPSWITCHER_URL, ROUTES_ADMIN, ROUTES_GETSTARTED } from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar";
import { useUserStore } from "@/stores/user";
import User, { UserLocalDevelopment } from "@/types/User";

const { t } = useI18n();

const query = ref<string>("");
const appswitcherBaseUrl = APPSWITCHER_URL;

const route = useRoute();
const snackbarStore = useSnackbarStore();
const userStore = useUserStore();
const [drawer, toggleDrawer] = useToggle();
const { hasWriterRole } = useRoleCheck();

// Check if current route is an admin route
const isAdminRoute = computed((): boolean => {
  return route.path.startsWith("/admin");
});

onMounted(() => {
  loadUser();
});

/**
 * Loads UserInfo from the backend and sets it in the store.
 */
function loadUser(): void {
  getUser()
    .then((user: User) => userStore.setUser(user))
    .catch(() => {
      // No user info received, so fallback
      if (import.meta.env.DEV) {
        userStore.setUser(UserLocalDevelopment());
      } else {
        userStore.setUser(null);
      }
    });
}

/**
 * Navigates to the page with the search results and sends an event to trigger further searches.
 */

async function search(): Promise<void> {
  if (query.value !== "" && query.value !== null) {
    snackbarStore.showMessage({
      message: "Sie haben nach " + query.value + " gesucht. ;)",
    });
  }
}
</script>
