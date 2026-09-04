import { assert, describe, test } from "vitest";

import { maxRule, minRule, uniqueRule } from "../../src/plugins/rules";

describe("min rule tests", () => {
  test("Throws error when too low", () => {
    // given
    const num = 5;
    const validationRule = minRule(10);

    // when
    const result = validationRule(num);

    // then
    assert.isString(result);
  });

  test("Throws no error when high enough", () => {
    // given
    const num = 10;
    const validationRule = minRule(10);

    // when
    const result = validationRule(num);

    // then
    assert.isTrue(result);
  });

  test("Throws no error when undefined", () => {
    // given
    const num = undefined;
    const validationRule = minRule(10);

    // when
    const result = validationRule(num);

    // then
    assert.isTrue(result);
  });
});

describe("max rule tests", () => {
  test("Throws error when too high", () => {
    // given
    const num = 15;
    const validationRule = maxRule(10);

    // when
    const result = validationRule(num);

    // then
    assert.isString(result);
  });

  test("Throws no error when low enough", () => {
    // given
    const num = 10;
    const validationRule = maxRule(10);

    // when
    const result = validationRule(num);

    // then
    assert.isTrue(result);
  });

  test("Throws no error when undefined", () => {
    // given
    const num = undefined;
    const validationRule = maxRule(10);

    // when
    const result = validationRule(num);

    // then
    assert.isTrue(result);
  });
});

describe("unique rule tests", () => {
  test("Throws an error when value already exists and no initialValue", () => {
    // given
    const newValue = 10;
    const existingValues = [5, 8, 10];
    const validationRule = uniqueRule(existingValues, undefined);

    // when
    const result = validationRule(newValue);

    // then
    assert.isString(result);
  });

  test("Throws no error when value does not exists", () => {
    // given
    const newValue = 10;
    const existingValues = [5, 8];
    const validationRule = uniqueRule(existingValues, undefined);

    // when
    const result = validationRule(newValue);

    // then
    assert.isTrue(result);
  });

  test("Throws no error when value still exists and is initialValue", () => {
    // given
    const newValue = 10;
    const existingValues = [5, 8, 10];
    const validationRule = uniqueRule(existingValues, newValue);

    // when
    const result = validationRule(newValue);

    // then
    assert.isTrue(result);
  });

  test("Throws no error when undefined", () => {
    // given
    const newValue = undefined;
    const existingValues = [5, 8, 10];
    const validationRule = uniqueRule(existingValues, newValue);

    // when
    const result = validationRule(newValue);

    // then
    assert.isTrue(result);
  });
});
