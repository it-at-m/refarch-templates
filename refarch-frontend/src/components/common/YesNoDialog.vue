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
          No
        </v-btn>
        <v-btn
          id="yesnodialog-btn-yes"
          color="primary"
          @click="yes"
        >
          Yes
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { computed } from "vue";

/**
 * The YesNo dialog is a generic dialog for yes/no queries to the user.
 * For example, it can be used to confirm the deletion of an entity.
 *
 * As confirming an action is usually associated with a button, the YesNoDialog offers this at the same time. This can be configured via `buttontext` and `icontext`.
 *
 * If both `buttontext` and `icontext` are not set, the YesNoDialog can also be used as a pure dialog. In this case, the value is passed through from the dialog.
 *
 * Confirmation of the dialog is signalled via a `yes` event. Similarly, the
 * rejection is signaled by a `no` event.
 *
 * Example:
 * <yes-no-dialog
 *    v-model="deleteDialog"
 *    buttontext="Delete"
 *    dialogtitle="Delete?"
 *    dialogtext="Do you really want to delete the entity?"
 *    @no="deleteDialog = false"
 *    @yes="deleteSome"></yes-no-dialog>
 */

const props = defineProps<{
  buttontext?: string;
  icontext?: string;
  dialogtitle: string;
  dialogtext: string;
  /**
   * Control flag for dialog
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
