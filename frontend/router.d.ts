import "vue-router";

import type { Role } from "@/types/Role";

// To ensure it is treated as a module, add at least one `export` statement
export {};

declare module "vue-router" {
  interface RouteMeta {
    hasAnyRole?: Role | Role[];
  }
}
