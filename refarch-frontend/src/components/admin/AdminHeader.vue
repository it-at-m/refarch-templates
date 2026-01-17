<template>
  <v-app-bar
    color="#333333"
    dark
  >
    <v-row align="center">
      <v-col
        cols="3"
        class="d-flex align-center justify-start"
      >
        <v-app-bar-nav-icon
          color="white"
          @click.stop="toggleDrawer()"
        />
        <router-link :to="{ name: ROUTES_ADMIN }">
          <v-toolbar-title class="font-weight-bold">
            <span class="text-white">CMS Admin</span>
          </v-toolbar-title>
        </router-link>
      </v-col>
      <v-col
        cols="9"
        class="d-flex align-center justify-end"
      >
        <v-btn
          variant="text"
          icon
          color="white"
        >
          <ad2-image-avatar
            v-if="userStore.getUser !== null"
            :username="userStore.getUser.username"
          />
        </v-btn>
      </v-col>
    </v-row>
  </v-app-bar>
  <v-navigation-drawer
    v-model="drawer"
    color="#ffffff"
  >
    <v-list>
      <v-list-item :to="{ name: ROUTES_HOME }">
        <v-list-item-title> Homepage </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import { useToggle } from "@vueuse/core";

import Ad2ImageAvatar from "@/components/common/Ad2ImageAvatar.vue";
import { ROUTES_ADMIN, ROUTES_HOME } from "@/constants";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
const [drawer, toggleDrawer] = useToggle();
</script>

<style scoped>
/* Admin header styles */
</style>
