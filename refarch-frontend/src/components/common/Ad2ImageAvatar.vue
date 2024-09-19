<template>
  <v-avatar>
    <img
      :src="avatarUrl"
      :alt="altText"
    />
  </v-avatar>
</template>
<script setup lang="ts">
import { computed } from "vue";

import { DefaultLhmAvatarService } from "@/api/Ad2ImageAvatarService";

const {
  username,
  avatarMode = "fallbackGeneric",
  avatarSize = "64",
} = defineProps<{
  username: string;
  avatarMode?: string;
  avatarSize?: string;
}>();

const avatarUrl = computed(() => {
  return DefaultLhmAvatarService.avatarHref(username, avatarMode, avatarSize);
});

const altText = computed(() => `Bild von ${username}`);
</script>
