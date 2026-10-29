import { defineCustomElement } from "vue";

import RefarchHelloWorldVueComponent from "@/refarch-hello-world-webcomponent.ce.vue";

// convert into custom element constructor
const RefarchHelloWorldWebComponent = defineCustomElement(
  RefarchHelloWorldVueComponent
);

// register
customElements.define(
  "refarch-hello-world-webcomponent",
  RefarchHelloWorldWebComponent
);
