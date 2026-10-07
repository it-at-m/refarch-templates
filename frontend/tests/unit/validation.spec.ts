import { beforeEach, describe, expect, test, vi } from "vitest";

import type {
  ValidationAttributes,
  VuetifyRuleAliases,
} from "../../src/util/validation";

import {
  getOpenAPIValidationConstraint,
  mapOpenAPIToVuetifyValidationRules,
} from "../../src/util/validation";

describe("mapOpenAPIToVuetifyValidationRules", () => {
  let rules: VuetifyRuleAliases;

  beforeEach(() => {
    rules = {
      required: vi.fn(() => "required-rule"),
      strictLength: vi.fn((length) => `strict-length-${length}`),
      minLength: vi.fn((length) => `min-length-${length}`),
      maxLength: vi.fn((length) => `max-length-${length}`),
      pattern: vi.fn((pattern) => pattern),
      number: vi.fn(() => "number-rule"),
      min: vi.fn((value, exclusive) => `min-${value}-${exclusive}`),
      max: vi.fn((value, exclusive) => `max-${value}-${exclusive}`),
    };
  });

  test.concurrent.for([
    {
      description: "returns no rules when the property does not exist",
      attributes: {
        name: {
          required: true,
        },
      },
      property: "unknown",
      expected: [],
    },
    {
      description: "adds the required rule",
      attributes: {
        name: {
          required: true,
        },
      },
      property: "name",
      expected: ["required-rule"],
    },
    {
      description: "does not add required when required is false",
      attributes: {
        name: {
          required: false,
        },
      },
      property: "name",
      expected: [],
    },
    {
      description: "maps equal minLength and maxLength to strictLength",
      attributes: {
        value: {
          minLength: 10,
          maxLength: 10,
        },
      },
      property: "value",
      expected: ["strict-length-10"],
    },
    {
      description: "maps minLength and maxLength independently",
      attributes: {
        value: {
          minLength: 2,
          maxLength: 20,
        },
      },
      property: "value",
      expected: ["min-length-2", "max-length-20"],
    },
    {
      description: "does not add string length rules for zero",
      attributes: {
        value: {
          minLength: 0,
          maxLength: 0,
        },
      },
      property: "value",
      expected: [],
    },
    {
      description: "adds the number rule for number data types",
      attributes: {
        value: {
          dataType: "number",
        },
      },
      property: "value",
      expected: ["number-rule"],
    },
    {
      description: "does not add the number rule for other data types",
      attributes: {
        value: {
          dataType: "string",
        },
      },
      property: "value",
      expected: [],
    },
    {
      description: "maps minimum to the custom min rule",
      attributes: {
        value: {
          minimum: 10,
        },
      },
      property: "value",
      expected: ["min-10-undefined"],
    },
    {
      description: "passes exclusiveMinimum to the min rule",
      attributes: {
        value: {
          minimum: 10,
          exclusiveMinimum: true,
        },
      },
      property: "value",
      expected: ["min-10-true"],
    },
    {
      description: "maps maximum to the custom max rule",
      attributes: {
        value: {
          maximum: 100,
        },
      },
      property: "value",
      expected: ["max-100-undefined"],
    },
    {
      description: "passes exclusiveMaximum to the max rule",
      attributes: {
        value: {
          maximum: 100,
          exclusiveMaximum: true,
        },
      },
      property: "value",
      expected: ["max-100-true"],
    },
  ])("$description", ({ attributes, property, expected }) => {
    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      property as never
    );

    expect(result).toEqual(expected);
  });

  test.concurrent.for([
    {
      rule: "min",
      attributes: {
        value: {
          minimum: 10,
        },
      },
    },
    {
      rule: "max",
      attributes: {
        value: {
          maximum: 100,
        },
      },
    },
  ])(
    "does not add custom rule '$rule' when unavailable",
    ({ rule, attributes }) => {
      const rulesWithoutRule = {
        ...rules,
        [rule]: undefined,
      };

      const result = mapOpenAPIToVuetifyValidationRules(
        rulesWithoutRule,
        attributes,
        "value"
      );

      expect(result).toEqual([]);
    }
  );

  test("maps pattern strings to regular expressions", () => {
    const attributes = {
      value: {
        pattern: "/^[A-Z]+$/",
      },
    };

    mapOpenAPIToVuetifyValidationRules(rules, attributes, "value");

    expect(rules.pattern).toHaveBeenCalledOnce();

    const regex = vi.mocked(rules.pattern).mock.calls[0][0];

    expect(regex).toBeInstanceOf(RegExp);
    expect(regex.source).toBe("^[A-Z]+$");
  });

  test("maps all applicable rules in the expected order", () => {
    const attributes = {
      value: {
        required: true,
        minLength: 2,
        maxLength: 20,
        pattern: "/^[a-z]+$/",
        dataType: "number",
        minimum: 1,
        exclusiveMinimum: true,
        maximum: 100,
        exclusiveMaximum: false,
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "value"
    );

    expect(result).toEqual([
      "required-rule",
      "min-length-2",
      "max-length-20",
      /^[a-z]+$/,
      "number-rule",
      "min-1-true",
      "max-100-false",
    ]);
  });
});

describe("getOpenAPIValidationConstraint", () => {
  interface TestCase {
    attributes: Record<string, ValidationAttributes>;
    property: string;
    constraint: keyof ValidationAttributes;
    expected: unknown;
  }

  test.concurrent.for<TestCase>([
    {
      attributes: {
        name: {
          required: true,
          maxLength: 100,
        },
      },
      property: "name",
      constraint: "maxLength",
      expected: 100,
    },
    {
      attributes: {
        name: {
          required: true,
          maxLength: 100,
        },
      },
      property: "name",
      constraint: "required",
      expected: true,
    },
    {
      attributes: {
        name: {},
      },
      property: "name",
      constraint: "maxLength",
      expected: undefined,
    },
    {
      attributes: {
        name: {
          required: true,
        },
      },
      property: "unknown",
      constraint: "required",
      expected: undefined,
    },
  ])(
    "returns $expected for $property.$constraint",
    ({ attributes, property, constraint, expected }) => {
      expect(
        getOpenAPIValidationConstraint(attributes, property, constraint)
      ).toBe(expected);
    }
  );
});
