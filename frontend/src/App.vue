<template>
  <v-app>
    <the-snackbar-queue />
    <the-app-bar @clicked-nav-icon="toggleNavigation" />
    <the-navigation-drawer v-model="isNavigationShown" />
    <v-main>
      <router-view v-slot="{ Component }">
        <v-fade-transition mode="out-in">
          <component :is="Component" />
        </v-fade-transition>
      </router-view>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { useToggle } from "@vueuse/core";

import TheAppBar from "@/components/TheAppBar.vue";
import TheNavigationDrawer from "@/components/TheNavigationDrawer.vue";
import TheSnackbarQueue from "@/components/TheSnackbarQueue.vue";

const [isNavigationShown, toggleNavigation] = useToggle();
</script>
