<template>
  <main>
    <div>
      <div v-html="mucIconsSprite"/>
      <div v-html="customIconsSprite"/>

      <MucCallout>
        <template #content>
          <p>{{ calloutContent }}</p>
        </template>
      </MucCallout>
    </div>
  </main>
</template>

<script setup lang="ts">
import { MucCallout } from "@muenchen/muc-patternlab-vue";
import { FIRSTNAME_DEFAULT } from "@/util/constants";
import { computed } from "vue";
import customIconsSprite from "@muenchen/muc-patternlab-vue/dist/assets/temporary/custom-icons.svg?raw";
import mucIconsSprite from "@muenchen/muc-patternlab-vue/dist/assets/temporary/muc-icons.svg?raw";

const props = withDefaults(
    defineProps<{
      firstName?: string;
    }>(),
    {
      firstName: FIRSTNAME_DEFAULT
    }
);

const calloutContent = computed(() => {
  return `Hello ${props.firstName}`;
})
</script>

<style>
  @import "@muenchen/muc-patternlab-vue/dist/assets/temporary/muenchende-style.css";
  /*@import "@muenchen/muc-patternlab-vue/dist/assets/temporary/muenchende-fontfaces.css";*/
  @import "@muenchen/muc-patternlab-vue/dist/assets/temporary/custom-style.css";
  @import "@muenchen/muc-patternlab-vue/dist/style.css";
</style>
