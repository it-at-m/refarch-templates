import { shallowMount } from "@vue/test-utils";
import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, test } from "vitest";

import TheSnackbarQueue from "@/components/TheSnackbarQueue.vue";
import i18n from "@/plugins/i18n";
import { useSnackbarStore } from "@/stores/snackbar";

describe("TheSnackbarQueue.vue", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  test("passes store queue to v-snackbar-queue", async () => {
    const pinia = createPinia();

    const wrapper = shallowMount(TheSnackbarQueue, {
      global: {
        plugins: [pinia, i18n],
      },
    });

    const store = useSnackbarStore();

    store.push({ message: "Test_Message" });
    await wrapper.vm.$nextTick();

    const queueComponent = wrapper.findComponent({ name: "v-snackbar-queue" });
    expect(queueComponent.props("modelValue")).toEqual(store.queue);
  });
});
