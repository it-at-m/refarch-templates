<template>
  <div>
    <!-- eslint-disable-next-line vue/no-v-html -->
    <div v-html="mucIconsSprite" />
    <!-- eslint-disable-next-line vue/no-v-html -->
    <div v-html="customIconsSprite" />
    <muc-callout>
      <template #header>
        <p>Welcome to the default template for refarch-webcomponent</p>
      </template>
      <template #content>
        <p>{{ calloutContent }}</p>
      </template>
    </muc-callout>
  </div>
</template>

<script setup lang="ts">
import { MucCallout } from "@muenchen/muc-patternlab-vue";
import customIconsSprite from "@muenchen/muc-patternlab-vue/assets/temporary/custom-icons.svg?raw";
import mucIconsSprite from "@muenchen/muc-patternlab-vue/assets/temporary/muc-icons.svg?raw";
import { computed } from "vue";

import { FIRSTNAME_DEFAULT } from "@/util/constants";

const { firstName = FIRSTNAME_DEFAULT } = defineProps<{
  firstName?: string;
}>();

const calloutContent = computed(() => {
  return `Hello ${firstName}`;
});
</script>

<style>
@import "@muenchen/muc-patternlab-vue/assets/temporary/muenchende-style.css";
@import "@muenchen/muc-patternlab-vue/assets/temporary/custom-style.css";
@import "@muenchen/muc-patternlab-vue/style.css";
</style>
