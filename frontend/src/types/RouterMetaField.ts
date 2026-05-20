import "vue-router";

export {};

declare module "vue-router" {
  interface RouteMeta {
    requiredRoles?: string | string[];
  }
}
