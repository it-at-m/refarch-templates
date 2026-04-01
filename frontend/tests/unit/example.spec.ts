import { createTestingPinia } from "@pinia/testing";
import { shallowMount } from "@vue/test-utils";
import { describe, expect, test, vi } from "vitest";

import TheSnackbarQueue from "@/components/TheSnackbarQueue.vue";
import i18n from "@/plugins/i18n";
import { useSnackbarStore } from "@/stores/snackbar";

describe("TheSnackbarQueue.vue", () => {
  test("passes store queue to v-snackbar-queue", async () => {
    const wrapper = shallowMount(TheSnackbarQueue, {
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vi.fn,
          }),
          i18n,
        ],
      },
    });

    const store = useSnackbarStore();

    store.push({ text: "Test_Message" });

    const queueComponent = wrapper.findComponent({ name: "v-snackbar-queue" });
    expect(queueComponent.props("modelValue")).toEqual(store.queue);
  });
});
