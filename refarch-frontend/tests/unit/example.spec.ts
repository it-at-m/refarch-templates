import { shallowMount } from "@vue/test-utils";
import { createPinia } from "pinia";
import { describe, expect, test } from "vitest";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import TheSnackbar from "@/components/TheSnackbar.vue";

const pinia = createPinia();
const vuetify = createVuetify({
  components,
  directives,
});

describe("TheSnackbar.vue", () => {
  test("renders props.message when passed", () => {
    const message = "Hello_World";
    const wrapper = shallowMount(TheSnackbar, {
      global: {
        plugins: [pinia, vuetify],
      },
      props: { message: message },
    });

    expect(wrapper.html()).toContain(message);
  });
});
