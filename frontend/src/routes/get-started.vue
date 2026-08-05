<template>
  <v-container class="fill-height d-flex align-center flex-wrap">
    <v-row class="text-center">
      <v-col>
        <h1 class="text-display-medium font-weight-bold mb-10">
          {{ t("views.getStarted.header") }}
        </h1>
        <h3>{{ t("views.getStarted.docsText") }}</h3>
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
      v-model="showDialog"
      :dialogtitle="t('views.getStarted.saveLeave.title')"
      :dialogtext="t('views.getStarted.saveLeave.text')"
      @no="cancel"
      @yes="leave"
    />
  </v-container>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { useI18n } from "vue-i18n";

import YesNoDialog from "@/components/common/YesNoDialog.vue";
import { useSaveLeave } from "@/composables/useSaveLeave";
import { Role } from "@/types/Role";

const { t } = useI18n();

definePage({
  meta: {
    hasAnyRole: [Role.READER, Role.WRITER],
  },
});

const documentationClicked = ref(false);
const isDirty = computed(() => !documentationClicked.value);
const { cancel, leave, showDialog } = useSaveLeave(isDirty);
</script>
