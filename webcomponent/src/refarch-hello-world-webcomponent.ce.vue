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
        <p>
          Das API-Gateway ist:
          <span :class="apiGwStatus">{{ apiGwStatus }}</span>
        </p>
        <p>
          Das Backend ist:
          <span :class="backendStatus">{{ backendStatus }}</span>
        </p>
      </template>
    </muc-callout>
  </div>
</template>

<script setup lang="ts">
import type HealthState from "@/types/HealthState.ts";

import { MucCallout } from "@muenchen/muc-patternlab-vue";
import customIconsSprite from "@muenchen/muc-patternlab-vue/assets/icons/custom-icons.svg?raw";
import mucIconsSprite from "@muenchen/muc-patternlab-vue/assets/icons/muc-icons.svg?raw";
import { computed, onMounted, ref } from "vue";

import { ApiFactory } from "@/api/ApiFactory.ts";
import { ActuatorApi } from "@/api/generated/refarch-backend";
import { checkHealth } from "@/api/health-client.ts";
import { FIRSTNAME_DEFAULT } from "@/util/constants";

const apiGwStatus = ref("DOWN");
const backendStatus = ref("DOWN");

const { firstName = FIRSTNAME_DEFAULT } = defineProps<{
  firstName?: string;
}>();

const calloutContent = computed(() => {
  return `Hello ${firstName}`;
});

onMounted(async () => {
  const contentApiGw = await checkHealth();
  apiGwStatus.value = contentApiGw.status;

  const contentBackend = await ApiFactory.getInstance(ActuatorApi).health();
  backendStatus.value = (contentBackend as HealthState).status;
});
</script>

<style>
@import url("https://assets.muenchen.de/mde/1.1.19/css/style.css");
@import "@muenchen/muc-patternlab-vue/assets/css/custom-style.css";
@import "@muenchen/muc-patternlab-vue/style.css";

.UP {
  color: limegreen;
}

.DOWN {
  color: lightcoral;
}
</style>
