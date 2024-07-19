<template>
  <v-app>
    <the-snackbar />
    <v-app-bar color="primary">
      <v-row align="center">
        <v-col
          cols="3"
          class="d-flex align-center justify-start"
        >
          <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
          <router-link to="/">
            <v-toolbar-title class="font-weight-bold">
              <span class="text-white">RefArch-</span>
              <span class="text-secondary">Kick</span>
              <span class="text-white">Starter</span>
            </v-toolbar-title>
          </router-link>
        </v-col>
        <v-col
          cols="6"
          class="d-flex align-center justify-center"
        >
          <v-text-field
            id="suchfeld"
            v-model="query"
            flat
            variant="solo-inverted"
            hide-details
            label="Suche"
            clearable
            prepend-inner-icon="mdi-magnify"
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
          />
          <v-btn
            variant="text"
            icon
          >
            <lhm-avatar
              v-if="userStore.getUser !== null"
              :username="userStore.getUser.username"
            />
          </v-btn>
        </v-col>
      </v-row>
    </v-app-bar>
    <v-navigation-drawer v-model="drawer">
      <v-list>
        <v-list-item :to="{ name: ROUTES_GETSTARTED }">
          <v-list-item-title>Get started</v-list-item-title>
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
import type { Info } from "@/api/InfoService";

import { AppSwitcher } from "@muenchen/appswitcher-vue";
import { onMounted, ref } from "vue";

import InfoService from "@/api/InfoService";
import UserService from "@/api/UserService";
import TheSnackbar from "@/components/TheSnackbar.vue";
import { ROUTES_GETSTARTED } from "@/Constants";
import { useSnackbarStore } from "@/stores/snackbar";
import { useUserStore } from "@/stores/user";
import User, { UserLocalDevelopment } from "@/types/User";
import LhmAvatar from "./components/common/LhmAvatar.vue";

const drawer = ref(true);
const query = ref<string>("");
const appswitcherBaseUrl = ref<string | null>(null);

const snackbarStore = useSnackbarStore();
const userStore = useUserStore();

onMounted(() => {
  loadUser();
  InfoService.getInfo()
    .then((content: Info) => {
      appswitcherBaseUrl.value = content.appswitcher.url;
    })
    .catch((error) => {
      snackbarStore.showMessage(error);
    });
});

/**
 * Lädt UserInfo vom Backend und setzt diese im Store.
 */
function loadUser(): void {
  UserService.getUser()
    .then((user: User) => userStore.setUser(user))
    .catch(() => {
      // Keine Userinfo gekriegt, also Fallback
      if (import.meta.env.DEV) {
        userStore.setUser(UserLocalDevelopment());
      } else {
        userStore.setUser(null);
      }
    });
}

//Navigiert zur Seite mit den Suchergebnissen und sendet ein Event zum Auslösen weiterer Suchen.
async function search(): Promise<void> {
  if (query.value !== "" && query.value !== null) {
    snackbarStore.showMessage({
      message: "Sie haben nach " + query.value + " gesucht. ;)",
    });
  }
}
</script>

<style>
.main {
  background-color: white;
}
</style>
