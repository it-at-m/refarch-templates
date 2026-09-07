// eslint-disable-next-line @typescript-eslint/no-unused-vars
import type { maxRule, minRule } from "@/plugins/rules";
import type { ValidationRule } from "vuetify";

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import useVuetifyRulesFromOpenAPI from "@/composables/useVuetifyRulesFromOpenAPI.ts";

/**
 * Type that holds a sub-set of attributes in generated *ValidationAttributesMap types via the OpenAPI generator.
 * Only the attributes that are mappable to a corresponding Vuetify {@link VuetifyRuleAliases} are included.
 * Type currently not exposed publicly by OpenAPIGenerator, thus custom type required, see https://github.com/OpenAPITools/openapi-generator/pull/24623
 */
export interface ValidationAttributes {
  dataType?: string;
  required?: boolean;
  maxLength?: number;
  minLength?: number;
  pattern?: string;
  maximum?: number;
  exclusiveMaximum?: boolean;
  minimum?: number;
  exclusiveMinimum?: boolean;
}

/**
 * Type that mimics supported OpenAPI-relevant Vuetify provided rules of type RuleAliases (as the type is not publicly exported).
 * Additionally, the type holds OpenAPI-relevant custom rules defined in `plugins/rules.ts`
 */
export interface VuetifyRuleAliases {
  required: () => ValidationRule;
  strictLength: (length: number) => ValidationRule;
  minLength: (length: number) => ValidationRule;
  maxLength: (length: number) => ValidationRule;
  pattern: (pattern: RegExp) => ValidationRule;
  number: () => ValidationRule;
  // OpenAPI supported custom rules
  min?: (value: number, exclusive?: boolean) => ValidationRule;
  max?: (value: number, exclusive?: boolean) => ValidationRule;
}

/**
 * Maps OpenAPIGenerator typescript-fetch created constraints defined in *ValidationAttributesMap to Vuetify {@link ValidationRule}s.
 *
 * The mapped Vuetify rules can be used with the `rules` property on Vuetify input components.
 *
 * **Note:** Prefer using the {@link useVuetifyRulesFromOpenAPI} Vue composable instead of calling this function directly.
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
 * @param rules all available {@link VuetifyRuleAliases} typically retrieved via `useRules` composable from Vuetify
 * @param validationAttributes a generated *ValidationAttributesMap object
 * @param property property of the model object to calculate the {@link ValidationRule}s for.
 */
export function mapOpenAPIToVuetifyValidationRules<
  T extends Record<string, ValidationAttributes>,
  K extends keyof T,
>(
  rules: VuetifyRuleAliases,
  validationAttributes: T,
  property: K
): ValidationRule[] {
  const attributes = validationAttributes[property];
  const result: ValidationRule[] = [];

  if (!attributes) {
    console.debug(
      `Validation property "${String(property)}" not found in ${JSON.stringify(validationAttributes)}"`
    );
    return [];
  }

  // Required
  if (attributes.required !== undefined && attributes.required) {
    result.push(rules.required());
  }

  // Strings
  if (
    attributes.minLength !== undefined &&
    attributes.maxLength !== undefined &&
    attributes.minLength === attributes.maxLength &&
    attributes.minLength > 0
  ) {
    result.push(rules.strictLength(attributes.minLength));
  } else {
    if (attributes.minLength !== undefined && attributes.minLength > 0) {
      result.push(rules.minLength(attributes.minLength));
    }

    if (attributes.maxLength !== undefined && attributes.maxLength > 0) {
      result.push(rules.maxLength(attributes.maxLength));
    }
  }

  if (attributes.pattern !== undefined) {
    const regex = new RegExp(attributes.pattern.replace(/^\/|\/$/g, ""));
    result.push(rules.pattern(regex));
  }

  // Numbers
  if (attributes.dataType === "number") {
    result.push(rules.number());
  }

  if (attributes.minimum !== undefined && rules.min) {
    result.push(rules.min(attributes.minimum, attributes.exclusiveMinimum));
  }

  if (attributes.maximum !== undefined && rules.max) {
    result.push(rules.max(attributes.maximum, attributes.exclusiveMaximum));
  }

  return result;
}

/**
 * Retrieves the value of an OpenAPI {@link ValidationAttributes} map for use in Vuetify input components (e.g. `counter` property)
 *
 * @param validationAttributes a generated *ValidationAttributesMap object
 * @param property property of the model object to calculate the {@link ValidationRule}s for.
 * @param constraint name of a constraint in {@link ValidationAttributes}
 */
export function getOpenAPIValidationConstraint<
  T extends Record<string, ValidationAttributes>,
  K extends keyof T,
  C extends keyof ValidationAttributes,
>(validationAttributes: T, property: K, constraint: C) {
  const attributes = validationAttributes[property];

  if (!attributes) {
    console.debug(
      `Validation property "${String(property)}" not found in ${JSON.stringify(validationAttributes)}"`
    );
    return undefined;
  }

  return attributes[constraint];
}
