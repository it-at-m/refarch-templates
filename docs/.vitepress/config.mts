import { defineConfig } from "vitepress";
import { withMermaid } from "vitepress-plugin-mermaid";

// https://vitepress.dev/reference/site-config
const vitepressConfig = defineConfig({
  title: "RefArch Templates",
  description: "Documentation for the RefArch Templates",
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
          { text: "Getting Started", link: "/getting-started" },
          { text: "Develop", link: "/develop" },
          { text: "Document", link: "/document" },
          { text: "Organize", link: "/organize" },
        ],
      },
      { text: "Contribute", link: "/contribute" },
    ],
    sidebar: [
      { text: "Getting Started", link: "/getting-started" },
      { text: "Develop", link: "/develop" },
      { text: "Document", link: "/document" },
      { text: "Organize", link: "/organize" },
    ],
    socialLinks: [
      { icon: "github", link: "https://github.com/it-at-m/refarch-templates" },
    ],
    outline: {
      level: "deep",
    },
    editLink: {
      pattern:
        "https://github.com/it-at-m/refarch-templates/blob/main/docs/:path",
      text: "View this page on GitHub",
    },
    search: {
      provider: "local",
    },
    footer: {
      message: `<a href="https://opensource.muenchen.de/impress.html">Impress and Contact</a>`,
    },
  },
});

export default withMermaid(vitepressConfig);
