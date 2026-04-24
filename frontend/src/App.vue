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
import type { UserInfo } from "@/types/UserInfo";

import { useToggle } from "@vueuse/core";
import { onMounted } from "vue";

import { getUserInfo } from "@/api/userinfo-client";
import TheAppBar from "@/components/TheAppBar.vue";
import TheNavigationDrawer from "@/components/TheNavigationDrawer.vue";
import TheSnackbarQueue from "@/components/TheSnackbarQueue.vue";
import { useUserInfoStore } from "@/stores/userinfo";

const userInfoStore = useUserInfoStore();
const [isNavigationShown, toggleNavigation] = useToggle();

onMounted(() => {
  loadUserInfo();
});

/**
 * Loads UserInfo from the backend and sets it in the store.
 */
function loadUserInfo(): void {
  getUserInfo().then((userInfo: UserInfo) =>
    userInfoStore.setUserInfo(userInfo)
  );
}
</script>
