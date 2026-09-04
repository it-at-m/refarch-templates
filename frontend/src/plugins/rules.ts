import type { Plugin } from "vue";

import { createRulesPlugin } from "vuetify";

import vuetify from "@/plugins/vuetify";

/**
 * Custom type required as CustomValidationRuleBuilder type from Vuetify is not accessible.
 */
type CustomRule<RuleArgs extends unknown[], RuleValue> = (
  ...args: RuleArgs
) => (value: RuleValue) => string | boolean;

/**
 * Custom type for use in {@link uniqueRule}.
 */
type UniqueRule = <T>(
  values: T[],
  initialValue?: T,
  err?: string
) => (value: T) => string | boolean;

/**
 * Creates a validation rule that ensures a value is not below a minimum.
 *
 * @param minNumber - Minimum allowed value
 * @param exclusive - Sets `value` to be strictly greater than `minNumber`
 * @param err - Optional custom error message
 * @returns A validation function returning `true` for valid values or an error
 *   message when the minimum is not met.
 */
const minRule: CustomRule<[number, boolean?, string?], number> = (
  minNumber,
  exclusive = false,
  err
) => {
  return (v) =>
    v == null ||
    (exclusive ? v > minNumber : v >= minNumber) ||
    err ||
    (exclusive
      ? `Der Wert muss größer als ${minNumber} sein.`
      : `Der Wert muss mindestens ${minNumber} betragen.`);
};

/**
 * Creates a validation rule that ensures a value does not exceed a maximum.
 *
 * @param maxNumber - Maximum allowed value
 * @param exclusive - Sets `value` to be strictly smaller than `maxNumber`
 * @param err - Optional custom error message
 * @returns A validation function returning `true` for valid values or an error
 *   message when the maximum is exceeded.
 */
const maxRule: CustomRule<[number, boolean?, string?], number> = (
  maxNumber,
  exclusive = false,
  err?
) => {
  return (v) =>
    v == null ||
    (exclusive ? v < maxNumber : v <= maxNumber) ||
    err ||
    (exclusive
      ? `Der Wert muss kleiner als ${maxNumber} sein.`
      : `Der Wert darf höchstens ${maxNumber} betragen.`);
};

/**
 * Creates a validation rule that ensures a value is unique within a array.
 *
 * The `initialValue` is treated as valid even if it already exists in `values`.
 * This is useful when editing an existing entity, where its current value
 * naturally appears in the list of existing values.
 *
 * @param values - Values against which the value is checked for uniqueness
 * @param initialValue - The current value of the entity being edited
 * @param err - Optional custom error message
 * @returns A validation function that returns `true` when the value is unique,
 *   or an error message when it is already present.
 */
const uniqueRule: UniqueRule = (values, initialValue = undefined, err?) => {
  return (v) =>
    v == null ||
    (initialValue != null && v === initialValue) ||
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
