import { defineConfig } from "vitepress";
import { withMermaid } from "vitepress-plugin-mermaid";

// https://vitepress.dev/reference/site-config
const vitepressConfig = defineConfig({
  title: "Refarch Vitepress init",
  description: "Documentation for the RefArch",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Home', link: '/' },
      { text: 'Examples', link: '/markdown-examples' }
    ],
    sidebar: [
      {
        text: 'Examples',
        items: [
          { text: 'Markdown Examples', link: '/markdown-examples' },
          { text: 'Runtime API Examples', link: '/api-examples' }
        ]
      }
    ],
    socialLinks: [
      { icon: 'github', link: 'https://github.com/vuejs/vitepress' }
    ],
    search: {
      provider: 'local'
    }
  }
});

export default withMermaid(vitepressConfig);