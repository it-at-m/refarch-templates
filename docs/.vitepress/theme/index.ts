import type { Theme } from "vitepress";

import DefaultTheme from "vitepress/theme";

import LhmThemeExtension from "./LhmThemeExtension.vue";

import "./style.css";

export default <Theme>{
  ...DefaultTheme,
  Layout: LhmThemeExtension,
};
