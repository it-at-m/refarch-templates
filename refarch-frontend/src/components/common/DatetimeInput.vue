<template>
  <div>
    <div class="text-overline">
      {{ label }}
    </div>
    <v-input
      :readonly="readonly"
      :hide-details="hideDetails"
      :rules="validierungsRegeln"
      :density="dense ? 'compact' : 'default'"
      :error="error"
      :error-messages="errorMessages"
      :persistent-hint="persistentHint"
      :hint="hint"
    >
      <v-row>
        <v-col cols="6">
          <v-text-field
            ref="day"
            v-model="day"
            required
            label="Datum"
            :readonly="readonly"
            :error="error"
            hide-details
            :density="dense ? 'compact' : 'default'"
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
            ref="time"
            v-model="time"
            required
            label="Zeit"
            :readonly="readonly"
            :error="error"
            hide-details
            :density="dense ? 'compact' : 'default'"
            :filled="filled"
            :outlined="outlined"
            type="time"
            @focusout="leaveInput"
            @focus="enterInput"
            @blur="sendInput"
          >
            <template
              v-if="clearable && !readonly"
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

/**
 * Das Date-Time-Input` Feld bietet eine Eingabemöglichkeit von Date-Times ohne zusätzliche
 * Dialoge und möglichst mit den nativen Features des Browsers, welche auch per Tastatur bedient werden können.
 *
 * Beispiel:
 * <datetime-input
 *   id="kontakt-field"
 *   v-model="kontaktDate"
 *   dense
 *   clearable
 *   label="Kontakt am"
 *   :rules="[(v: string) => !v || moment(v).isBefore(moment.now()) || 'Datum muss in der Vergangenheit liegen.']"
 * ></datetime-input>
 */

interface Props {
  modelValue: string;
  readonly: boolean;
  hideDetails: boolean;
  dense: boolean;
  filled: boolean;
  outlined: boolean;
  clearable: boolean;
  persistentHint: boolean;
  hint: string;
  label: string;
  rules: { (v: string): string | boolean }[];
}

const props = withDefaults(defineProps<Props>(), {
  readonly: false,
  hideDetails: false,
  dense: false,
  filled: false,
  outlined: false,
  clearable: true,
  persistentHint: false,
  hint: "",
  label: "",
  rules: () => [],
});

const day = ref<string | null>(null);
const time = ref<string | null>(null);
const error = ref(false);
const errorMessages = ref("");
const dateFilled = (): string | boolean =>
  checkBothFieldsFilled() || "Datum und Zeit muss ausgefüllt werden";

const emits = defineEmits<{
  (e: "update:modelValue", v: string | null): void;
}>();

const validierungsRegeln = computed(() => {
  if (props.rules) {
    return [...props.rules, dateFilled];
  } else {
    return [dateFilled];
  }
});

onMounted(() => {
  parseValue();
});

function clear(): void {
  errorMessages.value = "";
  time.value = null;
  day.value = null;
  emits("update:modelValue", getDate());
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
  if (props.modelValue) {
    const newDate = new Date(props.modelValue);
    day.value = parseDay(newDate);
    time.value = parseTime(newDate);
  } else {
    day.value = null;
    time.value = null;
  }
}

watch(
  () => props.modelValue,
  () => parseValue()
);

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
    emits("update:modelValue", getDate());
  }
}

function checkBothFieldsFilled(): boolean {
  return !!(time.value && day.value) || (!time.value && !day.value);
}
</script>
