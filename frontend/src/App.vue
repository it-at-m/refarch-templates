<template>
  <v-app>
    <the-snackbar-queue />
    <the-app-bar @clicked-nav-icon="toggleNavigation" />
    <the-navigation-drawer v-model="isNavigationShown" />
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
import type { User } from "@/types/User";

import { useToggle } from "@vueuse/core";
import { onMounted } from "vue";

import { getUser } from "@/api/user-client";
import TheAppBar from "@/components/TheAppBar.vue";
import TheNavigationDrawer from "@/components/TheNavigationDrawer.vue";
import TheSnackbarQueue from "@/components/TheSnackbarQueue.vue";
import { useUserStore } from "@/stores/user";
import { USER_LOCAL_DEVELOPMENT } from "@/types/User";

const userStore = useUserStore();
const [isNavigationShown, toggleNavigation] = useToggle();

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
        userStore.setUser(USER_LOCAL_DEVELOPMENT);
      } else {
        userStore.setUser(null);
      }
    });
}
</script>
