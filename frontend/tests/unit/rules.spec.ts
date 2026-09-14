import { assert, describe, test } from "vitest";

import { maxRule, minRule, uniqueRule } from "../../src/plugins/rules";

describe("min rule tests", () => {
  test.concurrent.for([
    { num: 5, expected: "error" },
    { num: 10, expected: "valid" },
    { num: undefined, expected: "valid" },
  ])("validates $num as $expected", ({ num, expected }) => {
    // given
    const validationRule = minRule(10);

    // when
    const result = validationRule(num);

    // then
    if (expected === "error") {
      assert.isString(result);
    } else {
      assert.isTrue(result);
    }
  });
});

describe("max rule tests", () => {
  test.concurrent.for([
    { num: 15, expected: "error" },
    { num: 10, expected: "valid" },
    { num: undefined, expected: "valid" },
  ])("validates $num as $expected", ({ num, expected }) => {
    // given
    const validationRule = maxRule(10);

    // when
    const result = validationRule(num);

    // then
    if (expected === "error") {
      assert.isString(result);
    } else {
      assert.isTrue(result);
    }
  });
});

describe("unique rule tests", () => {
  test.concurrent.for([
    {
      newValue: 10,
      existingValues: [5, 8, 10],
      initialValue: undefined,
      expected: "error",
    },
    {
      newValue: 10,
      existingValues: [5, 8],
      initialValue: undefined,
      expected: "valid",
    },
    {
      newValue: 10,
      existingValues: [5, 8, 10],
      initialValue: 10,
      expected: "valid",
    },
    {
      newValue: undefined,
      existingValues: [5, 8, 10],
      initialValue: undefined,
      expected: "valid",
    },
  ])(
    "validates $newValue as $expected",
    ({ newValue, existingValues, initialValue, expected }) => {
      // given
      const validationRule = uniqueRule(existingValues, initialValue);

      // when
      const result = validationRule(newValue);

      // then
      if (expected === "error") {
        assert.isString(result);
      } else {
        assert.isTrue(result);
      }
    }
  );
});
