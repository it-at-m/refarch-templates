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
