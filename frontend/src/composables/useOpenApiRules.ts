import type { ValidationAttributes } from "@/util/validation";
import type { ComputedRef, MaybeRefOrGetter } from "vue";
import type { ValidationRule } from "vuetify";

import { computed, toValue } from "vue";
import { useRules } from "vuetify";

import { mapOpenAPIToVuetifyValidationRules } from "@/util/validation";

export default function useOpenApiRules<
  T extends Record<string, ValidationAttributes>,
  K extends keyof T,
>(
  validationAttributesMap: MaybeRefOrGetter<T>,
  property: MaybeRefOrGetter<K>
): ComputedRef<ValidationRule[]>;

export default function useOpenApiRules<
  T extends Record<string, ValidationAttributes>,
>(
  validationAttributesMap: MaybeRefOrGetter<T>
): <K extends keyof T>(
  property: MaybeRefOrGetter<K>
) => ComputedRef<ValidationRule[]>;

/**
 * Creates Vuetify {@link ValidationRule}s from OpenAPI-generated validation
 * attributes.
 *
 * See supported rules in {@link VuetifyRuleAliases}.
 *
 * @param validationAttributesMap OpenAPI-generated validation attributes map
 * @param property Optional model property. If omitted, returns a factory function for the whole attributes map.
 */
export default function useOpenApiRules<
  T extends Record<string, ValidationAttributes>,
  K extends keyof T,
>(
  validationAttributesMap: MaybeRefOrGetter<T>,
  property?: MaybeRefOrGetter<K>
) {
  const rules = useRules();

  const calculateRules = (property: MaybeRefOrGetter<K>) =>
    computed(() =>
      mapOpenAPIToVuetifyValidationRules(
        rules,
        toValue(validationAttributesMap),
        toValue(property)
      )
    );

  return property === undefined ? calculateRules : calculateRules(property);
}
