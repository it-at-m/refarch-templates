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
          {{ t("views.home.header") }}
        </h1>
        <p>
          {{ t("views.home.apiGatewayStatus") }}
          <span :class="status">{{ status }}</span>
        </p>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";

import { checkHealth } from "@/api/health-client";
import { useSnackbarStore } from "@/stores/snackbar";
import HealthState from "@/types/HealthState";

const { t } = useI18n();

const snackbarStore = useSnackbarStore();
const status = ref("DOWN");

onMounted(() => {
  checkHealth()
    .then((content: HealthState) => (status.value = content.status))
    .catch((error) => {
      snackbarStore.showMessage(error);
    });
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
