<template>
  <div>
    <div class="text-overline">
      {{ label }}
    </div>
    <v-input
      :readonly="readonly"
      :hide-details="hideDetails"
      :rules="validationRules"
      :density="density"
      :error="error"
      :error-messages="errorMessages"
      :persistent-hint="persistentHint"
      :hint="hint"
    >
      <v-row>
        <v-col cols="6">
          <v-text-field
            v-model="day"
            required
            label="Datum"
            :readonly="readonly"
            :error="error"
            hide-details
            :density="density"
            :filled="filled"
            :outlined="outlined"
            type="date"
            @focusout="leaveInput"
            @focus="enterInput"
            @blur="sendInput"
          />
        </v-col>
        <v-col cols="6">
          <v-text-field
            v-model="time"
            required
            label="Zeit"
            :readonly="readonly"
            :error="error"
            hide-details
            :density="density"
            :filled="filled"
            :outlined="outlined"
            type="time"
            @focusout="leaveInput"
            @focus="enterInput"
            @blur="sendInput"
          >
            <template
              v-if="showClearButton"
              #append-inner
            >
              <v-btn
                icon
                :disabled="!modelValue"
                @click="clear"
              >
                <v-icon v-if="modelValue"> mdi-close </v-icon>
              </v-btn>
            </template>
          </v-text-field>
        </v-col>
      </v-row>
    </v-input>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import {
  VBtn,
  VCol,
  VIcon,
  VInput,
  VRow,
  VTextField,
} from "vuetify/components";

/**
 * The Date-Time-Input` field offers the possibility to enter date-times without additional
 * dialogs and, if possible, with the native features of the browser, which can also be operated using the keyboard.
 *
 * Example:
 * <datetime-input
 *   id="contact-field"
 *   v-model="contactDate"
 *   dense
 *   clearable
 *   label="Kontakt am"
 *   :rules="[(v: string) => !v || moment(v).isBefore(moment.now()) || 'Datum muss in der Vergangenheit liegen.']"
 * ></datetime-input>
 */

// required until https://github.com/vuetifyjs/vuetify/issues/16680 is fixed
type ValidationRules = InstanceType<typeof VInput>["$props"]["rules"];

const modelValue = defineModel<string | null>();

const {
  readonly = false,
  hideDetails = false,
  dense = false,
  filled = false,
  outlined = false,
  clearable = true,
  persistentHint = false,
  hint = "",
  label = "",
  rules = [],
} = defineProps<{
  readonly: boolean;
  hideDetails: boolean;
  dense: boolean;
  filled: boolean;
  outlined: boolean;
  clearable: boolean;
  persistentHint: boolean;
  hint: string;
  label: string;
  rules: ValidationRules;
}>();

const day = ref<string | null>(null);
const time = ref<string | null>(null);
const error = ref(false);
const errorMessages = ref("");

function dateFilled(): string | boolean {
  return checkBothFieldsFilled() || "Datum und Zeit muss ausgefüllt werden";
}

const validationRules = computed(() => {
  if (rules) {
    return [...rules, dateFilled];
  } else {
    return [dateFilled];
  }
});

onMounted(() => {
  parseValue();
});

const density = computed(() => (dense ? "compact" : "default"));

const showClearButton = computed(() => clearable && !readonly);

function clear(): void {
  errorMessages.value = "";
  time.value = null;
  day.value = null;
  modelValue.value = getDate();
}

function getDate(): string | null {
  if (day.value && time.value) {
    error.value = false;
    errorMessages.value = "";
    return new Date(day.value + "T" + time.value).toISOString();
  }

  return null;
}

function parseValue(): void {
  if (modelValue.value) {
    const newDate = new Date(modelValue.value);
    day.value = parseDay(newDate);
    time.value = parseTime(newDate);
  } else {
    day.value = null;
    time.value = null;
  }
}

watch(modelValue, parseValue);

function parseDay(timestamp: Date): string {
  return timestamp.toISOString().replace(/T.*/, "");
}

function parseTime(timestamp: Date): string {
  return timestamp.toLocaleTimeString(navigator.language, {
    hour: "2-digit",
    minute: "2-digit",
  });
}

function leaveInput(): void {
  if (!checkBothFieldsFilled()) {
    error.value = true;
    errorMessages.value = "Datum und Zeit muss ausgefüllt werden";
  }
}

function enterInput(): void {
  if (!checkBothFieldsFilled()) {
    error.value = false;
    errorMessages.value = "";
  }
}

function sendInput(): void {
  if (checkBothFieldsFilled()) {
    modelValue.value = getDate();
  }
}

function checkBothFieldsFilled(): boolean {
  return !!(time.value && day.value) || (!time.value && !day.value);
}
</script>
