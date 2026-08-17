// List of Vuetify components (see Vuetify API, e.g. https://vuetifyjs.com/en/api/v-btn/) to enforce data-test for
const INTERACTIVE_VUETIFY_COMPONENTS = [
  // Containment
  "VBtn",
  "VIconBtn",
  "VExpansionPanel",
  // Navigation
  "VAppBarNavIcon",
  "VFab",
  "VPagination",
  "VSpeedDial",
  "VTab",
  // Form Inputs & Controls
  "VAutocomplete",
  "VCheckbox",
  "VColorInput",
  "VCombobox",
  "VDateInput",
  "VFileInput",
  "VFileUpload",
  "VFileUploadDropzone",
  "VInput",
  "VNumberInput",
  "VOtpInput",
  "VOtpField",
  "VRadio",
  "VRangeSlider",
  "VSelect",
  "VSlider",
  "VSwitch",
  "VTextField",
  "VTextarea",
  // Data & Display
  "VCalendar",
  "VCheckboxBtn",
  "VTreeview",
  // Selection
  "VCarousel",
  "VStepperVerticalItem",
  // Feedback
  "VSnackbar",
  "VSnackbarQueue",
  // Pickers
  "VColorPicker",
  "VDatePicker",
  "VTimePicker",
];

const TEST_ATTRIBUTE = "data-test";

const requireDataTest = {
  meta: {
    type: "problem",
    schema: [
      {
        type: "object",
        properties: {
          components: {
            type: "array",
            items: {
              type: "string",
            },
          },
        },
        additionalProperties: false,
      },
    ],
    messages: {
      missing:
        "Interactive Vuetify component '{{ component }}' must have a data-test attribute.",
    },
  },
  create(context) {
    const [{ components }] = context.options;

    const parserServices = context.sourceCode.parserServices;

    // check if defineTemplateBodyVisitor provided by https://github.com/vuejs/vue-eslint-parser is available
    if (!parserServices.defineTemplateBodyVisitor) {
      return {};
    }

    return parserServices.defineTemplateBodyVisitor({
      VElement(node) {
        // normalize scanned component to PascalCase to support different usages in SFCs
        const componentName = toPascalCase(node.rawName);

        if (!components.includes(componentName)) {
          return;
        }

        const hasDataTest = node.startTag.attributes.some(
          (attribute) =>
            attribute.type === "VAttribute" &&
            attribute.key.name === TEST_ATTRIBUTE
        );

        if (!hasDataTest) {
          context.report({
            node,
            messageId: "missing",
            data: {
              component: componentName,
            },
          });
        }
      },
    });
  },
};

const plugin = {
  rules: {
    "require-data-test": requireDataTest,
  },
};

function createConfig(components) {
  return {
    plugins: {
      local: plugin,
    },
    rules: {
      "local/require-data-test": [
        "error",
        {
          components,
        },
      ],
    },
  };
}

export default {
  ...plugin,
  configs: {
    "flat/vuetify": createConfig(INTERACTIVE_VUETIFY_COMPONENTS),
  },
};

function toPascalCase(name) {
  return name
    .split("-")
    .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
    .join("");
}
