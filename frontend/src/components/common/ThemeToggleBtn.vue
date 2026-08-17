<template>
  <v-btn
    :icon="theme.global.current.value.dark ? mdiWeatherSunny : mdiWeatherNight"
    data-test="theme-toggle-btn"
    @click="toggleTheme"
  />
</template>

<script setup lang="ts">
import { mdiWeatherNight, mdiWeatherSunny } from "@mdi/js";
import { useTheme } from "vuetify";

const theme = useTheme();

function toggleTheme(e: MouseEvent) {
  theme.setTransitionOrigin(e.currentTarget as HTMLElement);
  theme.toggle();
}
</script>
