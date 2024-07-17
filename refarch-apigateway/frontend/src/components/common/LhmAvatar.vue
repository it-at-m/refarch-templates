<template>
  <v-avatar>
    <img
      :src="avatarUrl"
      :alt="'Bild von ' + props.username"
    />
  </v-avatar>
</template>
<script setup lang="ts">
import { computed } from "vue";

import { DefaultLhmAvatarService } from "@/api/LhmAvatarService";

const props = withDefaults(
  defineProps<{
    username: string;
    avatarMode?: string;
    avatarSize?: string;
  }>(),
  {
    avatarMode: "fallbackGeneric",
    avatarSize: "64",
  }
);

const avatarUrl = computed(() => {
  return DefaultLhmAvatarService.avatarHref(
    props.username,
    props.avatarMode,
    props.avatarSize
  );
});
</script>
