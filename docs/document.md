# Document

The templates include various tools and best practices to help you with documenting your software project.
Those will be further explained below.

## Prerequisites

- Node.js 22 LTS (`22.11.x` - `22.x.x`)

## Writing the documentation

We encourage to write your software documentation using Markdown (`.md`). This format is broadly supported, standard on GitHub and has good support in IDEs.
If you need further information about Markdown itself, please check out this [guide](https://www.markdownguide.org/).

By combining the use of Markdown with the Node-based tool [Vitepress](https://vitepress.dev/), we enable easy conversion of raw markdown files into aesthetically pleasing and easy to use static web pages.

In the templates repository you will find a `docs` folder. This folder holds your markdown files and other configuration files for Vitepress itself.
To get started and install the required tools execute `npm install` inside the folder.
For more information on Vitepress and its configuration options please check out its [Getting Started Guide](https://vitepress.dev/guide/getting-started).

If you want a preview of the documentation while writing, use the `npm run dev` script to spin up a web server serving your documentation.

## Checking for errors

To maintain high quality in your documentation we provide a script to run various checking tools.

By executing `npm run lint` the tool [markdownlint-cli](https://github.com/igorshubovych/markdownlint-cli) will be used to check validity of your markdown files.
The configuration file we provide (`.markdownlint.jsonc`) is almost default, but feel free adjust it to your own needs. Please check out the official documentation of [markdownlint](https://github.com/DavidAnson/markdownlint#optionsconfig) itself in this case.
For all other files (e.g. the Vitepress configuration file itself) [Prettier](https://prettier.io/) and its respective configuration file (`.prettierrc`) is used.

A lot of the reported errors can be automatically fixed by running `npm run fix`.
It's suggested to make sure your documentation does not contain errors before commiting to version control.

## Deploying to the web

Deploying the documentation to the web requires the markdown files to be converted into static `.html` files. You can use the npm script `npm run build` to build the documentation and check the final result on your local machine inside the generated `dist` folder.
You can then move this folder onto a web server manually.

For the ease of use we prefer the use of GitHubs CI/CD capabilities using workflows and actions. That's why the templates provide a workflow file to deploy your application to the web. This file can be found in the directory `.github/workflows/deploy-docs.yml`.
By default, the documentation is deployed whenever a push to the `main` branch occurs, but you can adjust the workflow file to your needs.
For further information about CI/CD-related topics, please check out the documentation for our [custom actions and workflows](https://github.com/it-at-m/.github).

To make the workflow work you also need to enable GitHub pages for your repository and select "GitHub Actions" as source. Please check of the offical [GitHub Pages documentation](https://pages.github.com/) for further information.
