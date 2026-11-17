import type { Linter, Rule } from "eslint";
import type { AST } from "vue-eslint-parser";

interface RequireDataTestPlugin {
  rules: typeof plugin.rules;
  configs: {
    "flat/vuetify": Linter.Config;
    "flat/muc-patternlab-vue": Linter.Config;
  };
}

interface RequireDataTestOptions {
  components?: string[];
}

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

// List of muc-patternlab-vue components (see https://it-at-m.github.io/muc-patternlab-vue/)
const INTERACTIVE_MUC_PATTERNLAB_VUE_COMPONENTS = [
  "MucButton",
  "MucCalendar",
  "MucFileDropzone",
  "MucLink",
  "MucModal",
  "MucSlider",
  "MucStepper",
  "MucAccordionItem",
  "MucCheckbox",
  "MucCounter",
  "MucInput",
  "MucRadioButton",
  "MucSelect",
  "MucTextArea",
];

const TEST_ATTRIBUTE = "data-test";

const requireDataTest: Rule.RuleModule = {
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
        "Interactive component '{{ component }}' must have a data-test attribute.",
    },
  },
  create(context: Rule.RuleContext) {
    const options = context.options[0] as RequireDataTestOptions | undefined;
    const components = options?.components ?? [];

    const parserServices = context.sourceCode.parserServices;

    // check if defineTemplateBodyVisitor provided by https://github.com/vuejs/vue-eslint-parser is available
    if (!parserServices.defineTemplateBodyVisitor) {
      return {};
    }

    return parserServices.defineTemplateBodyVisitor({
      VElement(node: AST.VElement) {
        // normalize scanned component to PascalCase to support different usages in SFCs
        const componentName = toPascalCase(node.rawName);

        if (!components.includes(componentName)) {
          return;
        }

        const hasDataTest = node.startTag.attributes.some((attribute) => {
          if (attribute.type !== "VAttribute") {
            return false;
          }

          if (!attribute.directive) {
            return attribute.key.name === TEST_ATTRIBUTE;
          }

          return (
            attribute.key.name.name === "bind" &&
            attribute.key.argument?.type === "VIdentifier" &&
            attribute.key.argument.name === TEST_ATTRIBUTE
          );
        });

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

function createConfig(components: readonly string[]): Linter.Config {
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

const requireDataTestPlugin: RequireDataTestPlugin = {
  ...plugin,
  configs: {
    "flat/vuetify": createConfig(INTERACTIVE_VUETIFY_COMPONENTS),
    "flat/muc-patternlab-vue": createConfig(
      INTERACTIVE_MUC_PATTERNLAB_VUE_COMPONENTS
    ),
  },
};

export default requireDataTestPlugin;

function toPascalCase(name: string) {
  return name
    .split("-")
    .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
    .join("");
}
