/**
 * The SaveLeave Composable can be used to prevent data loss due to unintentional navigation.
 *
 * Accepts a function `isDirty()`, in which it can be determined whether it is safe to navigate or whether a query should be sent to the user.
 * This query can be resolved via a dialog, for example. For this purpose, the SaveLeaveComposable
 * offers a `saveLeaveDialog` flag for this purpose. For generic dialogs, the SaveLeaveComposable already provides the title and
 * text for generic dialogs.
 *
 * The user's decision can be executed by calling `leave()` or `cancel()`.
 */
import type { NavigationGuardNext, RouteLocationNormalized } from "vue-router";

import { ref } from "vue";
import { onBeforeRouteLeave } from "vue-router";

export function useSaveLeave(isDirty: () => boolean) {
  const saveLeaveDialogTitle = ref("Ungespeicherte Änderungen");
  const saveLeaveDialogText = ref(
    "Es sind ungespeicherte Änderungen vorhanden. Wollen Sie die Seite verlassen?"
  );
  const saveLeaveDialog = ref(false);
  const isSave = ref(false);

  const nextRoute = ref<NavigationGuardNext | null>(null);

  onBeforeRouteLeave(
    (
      to: RouteLocationNormalized,
      from: RouteLocationNormalized,
      next: NavigationGuardNext
    ) => {
      if (isDirty() && !isSave.value) {
        saveLeaveDialog.value = true;
        nextRoute.value = next;
      } else {
        saveLeaveDialog.value = false;
        next();
      }
    }
  );

  function cancel(): void {
    saveLeaveDialog.value = false;
    if (nextRoute.value != null) {
      nextRoute.value(false);
    }
  }

  function leave(): void {
    if (nextRoute.value != null) {
      nextRoute.value();
    }
  }

  return {
    saveLeaveDialogTitle,
    saveLeaveDialogText,
    saveLeaveDialog,
    isSave,
    nextRoute,
    cancel,
    leave,
  };
}
