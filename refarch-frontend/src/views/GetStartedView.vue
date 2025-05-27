<template>
  <v-container>
    <v-row class="text-center">
      <v-col>
        <h1 class="text-h3 font-weight-bold mb-10">
          {{ t("views.getStarted.docsHeader") }}
        </h1>
        <h3>{{ t("views.getStarted.docsSubtext") }}</h3>
        <div>
          <a
            href="https://refarch.oss.muenchen.de/templates"
            target="_blank"
            rel="noopener noreferrer"
            @click="documentationClicked = true"
            >{{ t("views.getStarted.docsLinks.templates") }}</a
          >
        </div>
        <div>
          <a
            href="https://refarch.oss.muenchen.de/"
            target="_blank"
            rel="noopener noreferrer"
            @click="documentationClicked = true"
            >{{ t("views.getStarted.docsLinks.main") }}</a
          >
        </div>
      </v-col>
    </v-row>
    <yes-no-dialog
      v-model="saveLeaveDialog"
      :dialogtitle="t('views.getStarted.saveLeave.title')"
      :dialogtext="t('views.getStarted.saveLeave.text')"
      @no="cancel"
      @yes="leave"
    />
  </v-container>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useI18n } from "vue-i18n";

import YesNoDialog from "@/components/common/YesNoDialog.vue";
import { useSaveLeave } from "@/composables/saveLeave";

const { t } = useI18n();

const documentationClicked = ref(false);
const { cancel, leave, saveLeaveDialog } = useSaveLeave(isDirty);

function isDirty(): boolean {
  return !documentationClicked.value;
}
</script>
