<template>
  <v-dialog
    v-model="visible"
    persistent
    width="800"
  >
    <template #activator="{ props: open }">
      <template v-if="buttontext">
        <v-btn
          color="primary"
          v-bind="open"
        >
          {{ buttontext }}
        </v-btn>
      </template>
      <template v-else-if="icontext">
        <v-btn
          color="primary"
          v-bind="open"
        >
          <v-icon size="large">
            {{ icontext }}
          </v-icon>
        </v-btn>
      </template>
    </template>
    <v-card>
      <v-card-title>
        {{ dialogtitle }}
      </v-card-title>
      <v-card-text>
        {{ dialogtext }}
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          id="yesnodialog-btn-no"
          variant="text"
          @click="no"
        >
          Nein
        </v-btn>
        <v-btn
          id="yesnodialog-btn-yes"
          color="primary"
          @click="yes"
        >
          Ja
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { computed } from "vue";

/**
 * Der YesNo-Dialog ist ein generischer Dialog zur binären Abfrage beim Nutzer.
 * So kann z.B. die Bestätigung für das Löschen einer Entität damit realisiert werden.
 *
 * Da das Bestätigen einer Aktion in der Regel mit einem Button zusammenhängt, bietet der
 * YesNoDialog diesen gleich mit an. Über `buttontext` und `icontext` kann dieser konfiguriert werden.
 *
 * Wenn sowohl kein `buttontext` als auch `icontext` nicht gesetzt sind, kann der YesNoDialog auch
 * als reiner Dialog verwendet werden. Hierzu wird das Value vom Dialog durchgereicht.
 *
 * Die Bestätigung des Dialogs wird über ein `yes` Event signalisiert. Analog erfolgt die
 * Signalisierung der Abweisung durch ein `no` Event.
 *
 * Beispiel:
 * <yes-no-dialog
 *    v-model="deleteDialog"
 *    buttontext="Löschen"
 *    dialogtitle="Löschen?"
 *    dialogtext="Wollen Sie die Entität wirklich löschen?"
 *    @no="deleteDialog = false"
 *    @yes="deleteSome"></yes-no-dialog>
 */

const props = defineProps<{
  buttontext?: string;
  icontext?: string;
  dialogtitle: string;
  dialogtext: string;
  /**
   * Steuerflag für den Dialog
   */
  modelValue: boolean;
}>();

const emits = defineEmits<{
  (e: "no"): void;
  (e: "yes"): void;
  (e: "update:modelValue", v: boolean): void;
}>();

const visible = computed({
  get: () => props.modelValue,
  set: (v) => emits("update:modelValue", v),
});

function no(): void {
  emits("no");
}
function yes(): void {
  emits("yes");
}
</script>
