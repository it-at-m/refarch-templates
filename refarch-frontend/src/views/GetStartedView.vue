<template>
  <v-container>
    <v-row class="text-center">
      <v-col>
        <h1 class="text-h3 font-weight-bold mb-10">Dokumentation</h1>
        <h3>Weiterführende Links sind hier zu finden:</h3>
        <div>
          <a
            href="https://refarch.oss.muenchen.de/templates"
            target="_blank"
            rel="noopener noreferrer"
            @click="documentationClicked = true"
            >Doku RefArch-Templates</a
          >
        </div>
        <div>
          <a
            href="https://refarch.oss.muenchen.de/"
            target="_blank"
            rel="noopener noreferrer"
            @click="documentationClicked = true"
            >Doku RefArch</a
          >
        </div>
      </v-col>
    </v-row>
    <yes-no-dialog
      v-model="saveLeaveDialog"
      dialogtitle="Wirklich verlassen?"
      dialogtext="Wollen Sie wirklich nicht die Dokumentation anschauen?"
      @no="cancel"
      @yes="leave"
    />
  </v-container>
</template>

<script setup lang="ts">
import { ref } from "vue";

import YesNoDialog from "@/components/common/YesNoDialog.vue";
import { useSaveLeave } from "@/composables/saveLeave";

const documentationClicked = ref(false);
const { cancel, leave, saveLeaveDialog } = useSaveLeave(isDirty);

function isDirty(): boolean {
  return !documentationClicked.value;
}
</script>
