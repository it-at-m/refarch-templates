import type { MaybeRefOrGetter } from "vue";

import { onMounted, onUnmounted, ref, toValue } from "vue";
import { onBeforeRouteLeave, onBeforeRouteUpdate } from "vue-router";

/**
 * The useSaveLeave composable can be used to prevent data loss due to unintentional navigation.
 *
 * Accepts a Ref, Getter or plain value boolean that determines whether it is safe to navigate or whether a query should be sent to the user.
 * This query can be resolved via a dialog, for example. For this purpose the composable
 * offers a `showDialog` boolean.
 *
 * The user's decision can be executed by calling `leave()` or `cancel()`.
 */

export function useSaveLeave(isDirty: MaybeRefOrGetter<boolean>) {
  const showDialog = ref(false);

  const pendingNavigationDecision = ref<
    ((allowNavigation: boolean) => void) | null
  >(null);

  function onBeforeRouteChange() {
    if (!toValue(isDirty)) {
      showDialog.value = false;
      return true;
    } else {
      showDialog.value = true;
      return new Promise<boolean>((resolve) => {
        pendingNavigationDecision.value?.(false);
        pendingNavigationDecision.value = resolve;
      });
    }
  }

  onBeforeRouteLeave(onBeforeRouteChange);
  onBeforeRouteUpdate(onBeforeRouteChange);

  function cancel(): void {
    showDialog.value = false;
    pendingNavigationDecision.value?.(false);
    pendingNavigationDecision.value = null;
  }

  function leave(): void {
    showDialog.value = false;
    pendingNavigationDecision.value?.(true);
    pendingNavigationDecision.value = null;
  }

  function onBeforeUnload(event: BeforeUnloadEvent) {
    if (!toValue(isDirty)) {
      return;
    }

    event.preventDefault();
  }

  onMounted(() => {
    window.addEventListener("beforeunload", onBeforeUnload);
  });

  onUnmounted(() => {
    window.removeEventListener("beforeunload", onBeforeUnload);
  });

  return {
    showDialog,
    cancel,
    leave,
  };
}
