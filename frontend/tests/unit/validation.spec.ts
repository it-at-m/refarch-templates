import { beforeEach, describe, expect, it, vi } from "vitest";

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

  it("returns no rules when the property does not exist", () => {
    const attributes = {
      name: { required: true },
    };

    const debugSpy = vi
      .spyOn(console, "debug")
      .mockImplementation(() => undefined);

    expect(
      mapOpenAPIToVuetifyValidationRules(rules, attributes, "unknown" as never)
    ).toEqual([]);

    expect(debugSpy).toHaveBeenCalledOnce();
    expect(debugSpy).toHaveBeenCalledWith(
      'Validation property "unknown" not found in {"name":{"required":true}}"'
    );
  });

  it("adds the required rule", () => {
    const attributes = {
      name: { required: true },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "name"
    );

    expect(result).toEqual(["required-rule"]);
    expect(rules.required).toHaveBeenCalledOnce();
  });

  it("does not add the required rule when required is false", () => {
    const attributes = {
      name: { required: false },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "name"
    );

    expect(result).toEqual([]);
    expect(rules.required).not.toHaveBeenCalled();
  });

  it("maps equal minLength and maxLength to strictLength", () => {
    const attributes = {
      value: {
        minLength: 10,
        maxLength: 10,
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "value"
    );

    expect(result).toEqual(["strict-length-10"]);
    expect(rules.strictLength).toHaveBeenCalledWith(10);
    expect(rules.minLength).not.toHaveBeenCalled();
    expect(rules.maxLength).not.toHaveBeenCalled();
  });

  it("maps minLength and maxLength independently", () => {
    const attributes = {
      value: {
        minLength: 2,
        maxLength: 20,
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "value"
    );

    expect(result).toEqual(["min-length-2", "max-length-20"]);
    expect(rules.minLength).toHaveBeenCalledWith(2);
    expect(rules.maxLength).toHaveBeenCalledWith(20);
  });

  it("does not add string length rules for zero", () => {
    const attributes = {
      value: {
        minLength: 0,
        maxLength: 0,
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "value"
    );

    expect(result).toEqual([]);
    expect(rules.minLength).not.toHaveBeenCalled();
    expect(rules.maxLength).not.toHaveBeenCalled();
    expect(rules.strictLength).not.toHaveBeenCalled();
  });

  it("maps pattern strings to regular expressions", () => {
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

  it("maps patterns without slash delimiters", () => {
    const attributes = {
      value: {
        pattern: "^[A-Z]+$",
      },
    };

    mapOpenAPIToVuetifyValidationRules(rules, attributes, "value");

    expect(rules.pattern).toHaveBeenCalledWith(/^[A-Z]+$/);
  });

  it("adds the number rule for number data types", () => {
    const attributes = {
      value: {
        dataType: "number",
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "value"
    );

    expect(result).toEqual(["number-rule"]);
    expect(rules.number).toHaveBeenCalledOnce();
  });

  it("does not add the number rule for other data types", () => {
    const attributes = {
      value: {
        dataType: "string",
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "value"
    );

    expect(result).toEqual([]);
    expect(rules.number).not.toHaveBeenCalled();
  });

  it("maps minimum to the custom min rule", () => {
    const attributes = {
      value: {
        minimum: 10,
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "value"
    );

    expect(result).toEqual(["min-10-undefined"]);
    expect(rules.min).toHaveBeenCalledWith(10, undefined);
  });

  it("passes exclusiveMinimum to the min rule", () => {
    const attributes = {
      value: {
        minimum: 10,
        exclusiveMinimum: true,
      },
    };

    mapOpenAPIToVuetifyValidationRules(rules, attributes, "value");

    expect(rules.min).toHaveBeenCalledWith(10, true);
  });

  it("maps maximum to the custom max rule", () => {
    const attributes = {
      value: {
        maximum: 100,
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rules,
      attributes,
      "value"
    );

    expect(result).toEqual(["max-100-undefined"]);
    expect(rules.max).toHaveBeenCalledWith(100, undefined);
  });

  it("passes exclusiveMaximum to the max rule", () => {
    const attributes = {
      value: {
        maximum: 100,
        exclusiveMaximum: true,
      },
    };

    mapOpenAPIToVuetifyValidationRules(rules, attributes, "value");

    expect(rules.max).toHaveBeenCalledWith(100, true);
  });

  it("does not add min when the min rule is unavailable", () => {
    const rulesWithoutMin = {
      ...rules,
      min: undefined,
    };

    const attributes = {
      value: {
        minimum: 10,
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rulesWithoutMin,
      attributes,
      "value"
    );

    expect(result).toEqual([]);
  });

  it("does not add max when the max rule is unavailable", () => {
    const rulesWithoutMax = {
      ...rules,
      max: undefined,
    };

    const attributes = {
      value: {
        maximum: 100,
      },
    };

    const result = mapOpenAPIToVuetifyValidationRules(
      rulesWithoutMax,
      attributes,
      "value"
    );

    expect(result).toEqual([]);
  });

  it("maps all applicable rules in the expected order", () => {
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
  beforeEach(() => {
    vi.spyOn(console, "debug").mockImplementation(() => undefined);
  });

  it("returns the requested constraint", () => {
    const attributes: Record<string, ValidationAttributes> = {
      name: {
        required: true,
        maxLength: 100,
      },
    };

    expect(
      getOpenAPIValidationConstraint(attributes, "name", "maxLength")
    ).toBe(100);

    expect(getOpenAPIValidationConstraint(attributes, "name", "required")).toBe(
      true
    );
  });

  it("returns undefined when the constraint is not defined", () => {
    const attributes: Record<string, ValidationAttributes> = {
      name: {},
    };

    expect(
      getOpenAPIValidationConstraint(attributes, "name", "maxLength")
    ).toBeUndefined();
  });

  it("returns undefined when the property does not exist", () => {
    const attributes: Record<string, ValidationAttributes> = {
      name: {
        required: true,
      },
    };

    expect(
      getOpenAPIValidationConstraint(attributes, "unknown", "required")
    ).toBeUndefined();

    expect(console.debug).toHaveBeenCalledWith(
      expect.stringContaining('Validation property "unknown" not found')
    );
  });
});
