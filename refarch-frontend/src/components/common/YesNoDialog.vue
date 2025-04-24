<template>
  <v-dialog
    :model-value="modelValue"
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
import {
  VBtn,
  VCard,
  VCardActions,
  VCardText,
  VCardTitle,
  VDialog,
  VIcon,
  VSpacer,
} from "vuetify/components";

defineProps<{
  /**
   * Control flag for dialog
   */
  modelValue: boolean;
  buttontext?: string;
  icontext?: string;
  dialogtitle: string;
  dialogtext: string;
}>();

const emit = defineEmits<{
  no: [];
  yes: [];
}>();

function no(): void {
  emit("no");
}
function yes(): void {
  emit("yes");
}
</script>
