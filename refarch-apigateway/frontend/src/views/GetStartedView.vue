<template>
  <v-container>
    <v-row class="text-center">
      <v-col>
        <h1 class="text-h3 font-weight-bold mb-10">Dokumentation</h1>
        <h3>Weiterführende Links sind hier zu finden:</h3>
        <div>
          <a
            href="https://wiki.muenchen.de/anwendungsentwicklung/index.php/Barrakuda"
            target="_blank"
            rel="noopener noreferrer"
            @click="dokumentationClicked = true"
            >Barrakuda-Doku</a
          >
        </div>
        <div>
          <a
            href="https://wiki.muenchen.de/anwendungsentwicklung/index.php/Archetype_API-Gateway"
            target="_blank"
            rel="noopener noreferrer"
            @click="dokumentationClicked = true"
            >Doku Archetype API-Gateway</a
          >
        </div>
        <div>
          <a
            href="https://wiki.muenchen.de/anwendungsentwicklung/index.php/Archetype_Backend"
            target="_blank"
            rel="noopener noreferrer"
            @click="dokumentationClicked = true"
            >Doku Archetype Backend</a
          >
        </div>
        <div>
          <a
            href="https://git.muenchen.de/ccse/refarch-archetype"
            target="_blank"
            rel="noopener noreferrer"
            @click="dokumentationClicked = true"
            >Git-Repo Archetypes</a
          >
        </div>
        <div>
          <a
            href="https://wiki.muenchen.de/anwendungsentwicklung/index.php/JavaScript-Themen"
            target="_blank"
            rel="noopener noreferrer"
            @click="dokumentationClicked = true"
            >Javascript-Wiki</a
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

const dokumentationClicked = ref(false);
const { cancel, leave, saveLeaveDialog } = useSaveLeave(isDirty);

function isDirty(): boolean {
  return !dokumentationClicked.value;
}
</script>
