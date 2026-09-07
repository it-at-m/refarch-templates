import type { ValidationAttributes } from "@/util/validation";
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import type { ValidationRule } from "vuetify";

import { computed, toValue } from "vue";
import type { MaybeRefOrGetter, ComputedRef } from "vue";
import { useRules } from "vuetify";

import { mapOpenAPIToVuetifyValidationRules } from "@/util/validation";

export default function useVuetifyRulesFromOpenAPI<
    T extends Record<string, ValidationAttributes>,
    K extends keyof T,
>(
    validationAttributesMap: MaybeRefOrGetter<T>,
    property: MaybeRefOrGetter<K>,
): ComputedRef<ReturnType<typeof mapOpenAPIToVuetifyValidationRules>>;

export default function useVuetifyRulesFromOpenAPI<
    T extends Record<string, ValidationAttributes>,
>(
    validationAttributesMap: MaybeRefOrGetter<T>,
): <K extends keyof T>(
    property: MaybeRefOrGetter<K>,
) => ComputedRef<ReturnType<typeof mapOpenAPIToVuetifyValidationRules>>;

/**
 * Creates Vuetify {@link ValidationRule}s from OpenAPI-generated validation
 * attributes.
 *
 * This composable is the preferred way to use OpenAPI validation attributes
 * with Vuetify input components. It wraps
 * {@link mapOpenAPIToVuetifyValidationRules} and automatically provides the
 * Vuetify rules from {@link useRules}.
 *
 * The returned rules are reactive and are recalculated whenever the validation
 * attributes or property changes.
 *
 * The composable supports two usage patterns:
 *
 * - Pass both `validationAttributesMap` and `property` to directly obtain a
 *   computed set of validation rules.
 * - Pass only `validationAttributesMap` to obtain a function that can be used
 *   to create computed validation rules for individual properties.
 *
 * Supported rules currently are:
 * - {@link VuetifyRuleAliases.required}
 * - {@link VuetifyRuleAliases.strictLength}
 * - {@link VuetifyRuleAliases.minLength}
 * - {@link VuetifyRuleAliases.maxLength}
 * - {@link VuetifyRuleAliases.pattern}
 * - {@link VuetifyRuleAliases.number}
 * - Custom {@link minRule} implementation
 * - Custom {@link maxRule} implementation
 *
 * @param validationAttributesMap a generated *ValidationAttributesMap object,
 * or a reactive reference/getter returning one.
 * @param property the property of the model object to calculate
 * {@link ValidationRule}s for, or a reactive reference/getter returning one.
 *
 * @returns A computed set of {@link ValidationRule}s when `property` is
 * provided, or a function for creating computed validation rules for a
 * property when it is omitted.
 */
export default function useVuetifyRulesFromOpenAPI<
    T extends Record<string, ValidationAttributes>,
    K extends keyof T,
>(
    validationAttributesMap: MaybeRefOrGetter<T>,
    property?: MaybeRefOrGetter<K>,
) {
    const rules = useRules();

    const calculateRules = (property: MaybeRefOrGetter<K>) =>
        computed(() =>
            mapOpenAPIToVuetifyValidationRules(
                rules,
                toValue(validationAttributesMap),
                toValue(property),
            ),
        );

    return property === undefined
        ? calculateRules
        : calculateRules(property);
}
