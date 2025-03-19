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
import customIconsSprite from "@muenchen/muc-patternlab-vue/assets/icons/custom-icons.svg?raw";
import mucIconsSprite from "@muenchen/muc-patternlab-vue/assets/icons/muc-icons.svg?raw";
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
@import url("https://assets.muenchen.de/mde/1.0.10/css/style.css");
@import "@muenchen/muc-patternlab-vue/assets/css/custom-style.css";
@import "@muenchen/muc-patternlab-vue/style.css";
</style>
