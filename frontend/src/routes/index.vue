<template>
  <v-container>
    <v-row class="text-center">
      <v-col cols="12">
        <v-img
          src="@/assets/logo.png"
          class="my-3"
          height="200"
        />
      </v-col>

      <v-col class="mb-4">
        <h1 class="text-h3 font-weight-bold mb-3">
          {{ t("views.index.header") }}
        </h1>
        <p>
          {{ t("views.index.apiGatewayStatus") }}
          <span :class="apiGwStatus">{{ apiGwStatus }}</span>
        </p>
        <p>
          {{ t("views.index.backendStatus") }}
          <span :class="backendStatus">{{ backendStatus }}</span>
        </p>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";

import { ActuatorApi } from "@/api/generated/refarch-backend";
import { checkHealth } from "@/api/health-client";
import { STATUS_INDICATORS } from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar";
import HealthState from "@/types/HealthState";
import { ApiFactory } from "@/util/apiFactory";

const { t } = useI18n();

const snackbarStore = useSnackbarStore();
const apiGwStatus = ref("DOWN");
const backendStatus = ref("DOWN");

onMounted(async () => {
  try {
    const content = await checkHealth();
    apiGwStatus.value = content.status;
  } catch (error) {
    const err = error as Error;
    snackbarStore.push({
      text: err.message,
      color: STATUS_INDICATORS.ERROR,
    });
  }

  try {
    const content = await ApiFactory.getInstance(ActuatorApi).health();
    backendStatus.value = (content as HealthState).status;
  } catch (error) {
    const err = error as Error;
    snackbarStore.push({
      text: err.message,
      color: STATUS_INDICATORS.ERROR,
    });
  }
});
</script>

<style scoped>
.UP {
  color: limegreen;
}

.DOWN {
  color: lightcoral;
}
</style>
