/**
 * The useSaveLeave composable can be used to prevent data loss due to unintentional navigation.
 *
 * Accepts a Ref, Getter or plain value boolean that determines whether it is safe to navigate or whether a query should be sent to the user.
 * This query can be resolved via a dialog, for example. For this purpose the composable
 * offers a `showDialog` boolean. The composable also provides the title and text for generic dialogs.
 *
 * The user's decision can be executed by calling `leave()` or `cancel()`.
 */
import type { MaybeRefOrGetter } from "vue";

import { ref, toValue } from "vue";
import { onBeforeRouteLeave } from "vue-router";

export function useSaveLeave(isDirty: MaybeRefOrGetter<boolean>) {
  const dialogTitle = "Ungespeicherte Änderungen";
  const dialogText =
    "Es sind ungespeicherte Änderungen vorhanden. Wollen Sie die Seite verlassen?";
  const showDialog = ref(false);

  const pendingNavigationDecision = ref<
    ((allowNavigation: boolean) => void) | null
  >(null);

  onBeforeRouteLeave(() => {
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
  });

  function cancel(): void {
    showDialog.value = false;
    pendingNavigationDecision.value?.(false);
    pendingNavigationDecision.value = null;
  }

  function leave(): void {
    pendingNavigationDecision.value?.(true);
    pendingNavigationDecision.value = null;
  }

  return {
    dialogTitle,
    dialogText,
    showDialog,
    cancel,
    leave,
  };
}
