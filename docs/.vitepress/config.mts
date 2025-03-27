import { defineConfig } from "vitepress";
import { withMermaid } from "vitepress-plugin-mermaid";

// https://vitepress.dev/reference/site-config
const vitepressConfig = defineConfig({
  title: "RefArch Docs Template",
  description: "Documentation template from the RefArch Templates",
  head: [
    [
      "link",
      {
        rel: "icon",
        href: `https://assets.muenchen.de/logos/lhm/icon-lhm-muenchen-32.png`,
      },
    ],
  ],
  lastUpdated: true,
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: "Home", link: "/" },
      {
        text: "Docs",
        items: [
          { text: "Example", link: "/example" },
          { text: "External link", link: "https://refarch.oss.muenchen.de" },
        ],
      },
    ],
    sidebar: [
      { text: "Example", link: "/example" },
      { text: "External link", link: "https://refarch.oss.muenchen.de" },
    ],
    socialLinks: [
      { icon: "github", link: "https://github.com/it-at-m/refarch-templates" },
    ],
    editLink: {
      pattern:
        "https://github.com/it-at-m/refarch-templates/blob/main/docs/:path",
      text: "View this page on GitHub",
    },
    footer: {
      message: `<a href="https://opensource.muenchen.de/impress.html">Impress and Contact</a>`,
    },
    outline: {
      level: "deep",
    },
    search: {
      provider: "local",
    },
  },
  markdown: {
    image: {
      lazyLoading: true,
    },
  },
});

export default withMermaid(vitepressConfig);
