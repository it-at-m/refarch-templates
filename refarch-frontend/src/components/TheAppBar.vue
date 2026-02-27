<template>
  <v-app-bar color="primary">
    <v-row align="center">
      <v-col
        cols="3"
        class="d-flex align-center justify-start"
      >
        <v-app-bar-nav-icon
          @click="emit('clickedNavIcon')"
          class="mx-2"
        />
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
          v-if="APPSWITCHER_URL"
          :base-url="APPSWITCHER_URL"
          :tags="['global']"
          :icon="mdiApps"
        />
        <v-btn
          v-if="userStore.getUser !== null"
          class="mx-2"
          variant="text"
          icon
        >
          <ad2-image-avatar :username="userStore.getUser.username" />
        </v-btn>
      </v-col>
    </v-row>
  </v-app-bar>
</template>

<script setup lang="ts">
import { mdiApps, mdiMagnify } from "@mdi/js";
import { AppSwitcher } from "@muenchen/appswitcher-vue";
import { ref } from "vue";
import { useI18n } from "vue-i18n";

import Ad2ImageAvatar from "@/components/common/Ad2ImageAvatar.vue";
import { APPSWITCHER_URL } from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar.ts";
import { useUserStore } from "@/stores/user.ts";

const userStore = useUserStore();
const snackbarStore = useSnackbarStore();
const { t } = useI18n();

const query = ref<string>("");

async function search(): Promise<void> {
  if (query.value !== "" && query.value !== null) {
    snackbarStore.push({
      text: "Sie haben nach " + query.value + " gesucht. ;)",
    });
  }
}

const emit = defineEmits<{
  clickedNavIcon: [];
}>();
</script>
