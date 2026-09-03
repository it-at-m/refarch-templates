import type { Plugin } from "vue";

import { createRulesPlugin } from "vuetify";

import vuetify from "@/plugins/vuetify";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
type CustomRule = (...args: any[]) => (value: any) => string | boolean;

/**
 * Creates a validation rule that ensures a value is not below a minimum.
 *
 * By default, the minimum value is inclusive, meaning values equal to
 * `minNumber` are considered valid. When `exclusive` is `true`, the value
 * must be strictly greater than `minNumber`.
 *
 * @param minNumber - Minimum allowed value.
 * @param exclusive - Whether `minNumber` itself should be considered invalid.
 * @param err - Custom error message returned when the value is below the minimum.
 * @returns A validation function returning `true` for valid values or an error
 *   message when the minimum is not met.
 */
const minRule: CustomRule = (
  minNumber: number,
  exclusive = false,
  err?: string
) => {
  return (v) =>
    exclusive
      ? v > minNumber || err || `Der Wert muss größer als ${minNumber} sein.`
      : v >= minNumber ||
        err ||
        `Der Wert muss mindestens ${minNumber} betragen.`;
};

/**
 * Creates a validation rule that ensures a value does not exceed a maximum.
 *
 * By default, the maximum value is inclusive, meaning values equal to
 * `maxNumber` are considered valid. When `exclusive` is `true`, the value
 * must be strictly less than `maxNumber`.
 *
 * @param maxNumber - Maximum allowed value.
 * @param exclusive - Whether `maxNumber` itself should be considered invalid.
 * @param err - Custom error message returned when the value exceeds the maximum.
 * @returns A validation function returning `true` for valid values or an error
 *   message when the maximum is exceeded.
 */
const maxRule: CustomRule = (
  maxNumber: number,
  exclusive = false,
  err?: string
) => {
  return (v) =>
    exclusive
      ? v < maxNumber || err || `Der Wert muss kleiner als ${maxNumber} sein.`
      : v <= maxNumber ||
        err ||
        `Der Wert darf höchstens ${maxNumber} betragen.`;
};

/**
 * Creates a validation rule that ensures a value is unique within a array.
 *
 * The `initialValue` is treated as valid even if it already exists in `values`.
 * This is useful when editing an existing entity, where its current value
 * naturally appears in the list of existing values.
 *
 * @param values - Values against which the value is checked for uniqueness.
 * @param initialValue - The current value of the entity being edited. This
 *   value is considered valid even if it is included in `values`.
 * @param err - Optional custom error message returned when the value is not unique.
 * @returns A validation function that returns `true` when the value is unique,
 *   or an error message when it is already present.
 */
const uniqueRule: CustomRule = (
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  values: any[],
  initialValue = undefined,
  err?: string
) => {
  return (v) =>
    (initialValue !== undefined && v === initialValue) ||
    !values.includes(v) ||
    err ||
    `Der Wert ${v} ist bereits vorhanden.`;
};

/**
 * Plugin that registers Vuetify-provided rules (see https://v4.vuetifyjs.com/en/features/rules/#api) and custom validation rules for use with
 * the `:rules` prop of Vuetify form components.
 *
 * Registered rules are available through the `useRules` composable, e.g.:
 * - `rules.maxLength(5)` for a Vuetify provided rule
 * - `rules['max']!(5, false)`
 *
 * More information in https://vuetifyjs.com/en/features/rules/
 */
export default createRulesPlugin(
  {
    aliases: {
      min: minRule,
      max: maxRule,
      unique: uniqueRule,
    },
  },
  vuetify.locale
) as Plugin;

// Test-only exports: exposed to allow unit tests to import the individual rules.
export { minRule, maxRule, uniqueRule };
