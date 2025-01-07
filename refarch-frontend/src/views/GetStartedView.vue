<template>
  <v-container>
    <v-row class="text-center">
      <v-col>
        <h1 class="text-h3 font-weight-bold mb-10">
          {{ t("views.getStarted.dokuHeader") }}
        </h1>
        <h3>{{ t("views.getStarted.dokuSubtext") }}</h3>
        <div>
          <a
            href="https://wiki.muenchen.de/anwendungsentwicklung/index.php/Barrakuda"
            target="_blank"
            rel="noopener noreferrer"
            @click="documentationClicked = true"
            >{{ t("views.getStarted.dokuLinks.barrakuda") }}</a
          >
        </div>
        <div>
          <a
            href="https://wiki.muenchen.de/anwendungsentwicklung/index.php/Archetype_API-Gateway"
            target="_blank"
            rel="noopener noreferrer"
            @click="documentationClicked = true"
            >{{ t("views.getStarted.dokuLinks.apiGateway") }}</a
          >
        </div>
        <div>
          <a
            href="https://wiki.muenchen.de/anwendungsentwicklung/index.php/Archetype_Backend"
            target="_blank"
            rel="noopener noreferrer"
            @click="documentationClicked = true"
            >{{ t("views.getStarted.dokuLinks.backend") }}</a
          >
        </div>
        <div>
          <a
            href="https://git.muenchen.de/ccse/refarch-archetype"
            target="_blank"
            rel="noopener noreferrer"
            @click="documentationClicked = true"
            >{{ t("views.getStarted.dokuLinks.archetypesRepo") }}</a
          >
        </div>
        <div>
          <a
            href="https://wiki.muenchen.de/anwendungsentwicklung/index.php/JavaScript-Themen"
            target="_blank"
            rel="noopener noreferrer"
            @click="documentationClicked = true"
            >{{ t("views.getStarted.dokuLinks.javascriptWiki") }}</a
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
import { VCol, VContainer, VRow } from "vuetify/components";

import YesNoDialog from "@/components/common/YesNoDialog.vue";
import { useSaveLeave } from "@/composables/saveLeave";

const { t } = useI18n();

const documentationClicked = ref(false);
const { cancel, leave, saveLeaveDialog } = useSaveLeave(isDirty);

function isDirty(): boolean {
  return !documentationClicked.value;
}
</script>
