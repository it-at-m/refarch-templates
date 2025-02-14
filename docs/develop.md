# Development

We include various development tools to simplify the development process and make development more comfortable and error-resistant.
Those tools will be further explained below.

::: danger IMPORTANT
Please make sure you worked through the corresponding [Getting Started](./getting-started#documentation) instructions before proceeding.
:::

## CI/CD Configurations

TBD

## CODEOWNERS

The **CODEOWNERS** file (found under the `.github` directory of the template) is an essential tool in version control systems like GitHub. 

It specifies who is responsible for different parts of the project, ensuring that the right people are involved in code reviews.
When modifications are made to these files, the designated owners receive a review request automatically, enhancing code quality and accountability.

Each line identifies a file or directory along with the owner(s) using their GitHub usernames or team names.

::: danger IMPORTANT
Please alter the CODEOWNERS file to list project members or team names for your own project.
Otherwise the [RefArch maintainer team (only accessible for it-at-m members)](https://github.com/orgs/it-at-m/teams/refarch-maintainer) has to approve all your code changes.
You definitely don't want that, as we are super nitpicky when it comes to code quality. ;)
:::

## Pull Request Tooling

When a pull request (PR) is created, several tools help maintain code quality:

### Code Rabbit

**Code Rabbit** is an AI-powered code reviewer that assists with PR assessments. The configuration file can be found at the root of the templates in `.coderabbit.yaml`. 

Our configuration enables automatic reviews (and follow-up reviews when changes to a PR have been made). Additionally, we set CodeRabbit in "nitpicky" mode to find all of those nasty bugs.
Feel free to customize the configuration to your own needs. More information is available in the [official documentation](https://docs.coderabbit.ai/).

::: info Information
To make CodeRabbit work make sure that it has access to your GitHub repository. For projects in the `it-at-m` organization CodeRabbit automatically has access and is enabled when the configuration file is found in your repository.
:::

### CodeQL

**CodeQL** is a GitHub tool for discovering vulnerabilities and code smells in code. More details can be found [here](https://codeql.github.com/).

The template enables CodeQL for Pull Requests and configures CodeQL to only scan for Java and JavaScript/TypeScript/Vue files by default.
For further information on how to change the configuration, please check out the documentation of the related custom [GitHub workflow](https://github.com/it-at-m/.github/blob/main/workflow-templates/codeql.yaml).

::: danger IMPORTANT
If you are using Java based projects inside your repository, your need to add those to the `java-build-path` variable pointing to the directory of the `pom.xml` files.
:::

### Dependency Review

To ensure that only dependencies with approved licenses are included, a [global check](https://github.com/it-at-m/.github/blob/main/workflow-configs/dependency_review.yaml) is implemented. The allowed licenses can be viewed [here](https://opensource.muenchen.de/de/licenses.html#einbindung-in-eigenentwicklungen).

### GitHub Rules

It is recommended to review the rulesets for pushing and merging in the GitHub repository. Depending on the project's branching strategy, some branches should be protected to prevent force pushes and merging without approval. These settings can be modified under `Settings > Rules > Rulesets`.

## LCM Tooling

To keep Lifecycle Management (LCM) up to date, [Renovate](https://docs.renovatebot.com/) is used. The bot is configured globally with [custom settings](https://github.com/it-at-m/.github/tree/main/renovate-configs) and repository-specific settings [here](https://github.com/it-at-m/refarch/blob/main/refarch-tools/refarch-renovate/refarch-renovate-config.json5).

These configurations ensure that a branch for each dependency upgrade is created for every project. For example, if two projects use `spotless-maven-plugin`, two separate branches will be created.

To modify the default Renovate settings, the `renovate.json5` file in the project's root directory can be edited.

::: danger IMPORTANT
Renovate will automatically merge created merge requests (MRs) that have been approved. To activate this feature, the first PR must be merged manually.
:::

## Technologies

Key technologies used in the templates include:

### Vite

[Vite](https://vite.dev/) is used as the build tool, along with the testing framework [Vitest](https://vitest.dev/).

### PatternLab

For web component development and integration with the official Munich website, [PatternLab](https://it-at-m.github.io/muc-patternlab-vue/?path=/docs/getting-started--docs) is utilized.

### Vue Dev Tools

The [Vue Dev Tools](https://devtools.vuejs.org/) provide useful features when developing with Vue.js. Those include checking and editing component state, debugging the Pinia store, testing client-side routing, inspecting page elements and way more.

The Vue Dev Tools are included as a development dependency inside the templates, so no further installation is required.

A useful feature is the inspection of elements, which allows to click components of your webpage inside your Browser-rendered application and open the relevant part right in your IDE.
To make use of this feature a few steps have to be made on your machine.

::: info  Information
If you use Visual Studio Code, no further configuration has to be done. You can simply ignore the steps mentioned below.
:::

Steps to set up the IDE connection for Dev Tools:
1. Make sure your IDE of choice can be accessed via your terminal environment (Some installers automatically add your IDE to the `PATH` variable, for some cases you might have to add it manually)
2. Add a new environment variable for your shell environment called `LAUNCH_EDITOR` (depending on your operating system you can use files like `.bashrc` or the management feature of your OS)
3. Set the value of `LAUNCH_EDITOR` to the name of your IDE executable (e.g. `idea`, `webstorm`, `codium`, `notepad++`)
4. Make sure the environment variable is loaded (you might have to re-login into your user account depending on your OS)

::: info Information
Not all IDEs are supported right now, please check out [supported editors](https://github.com/webfansplz/vite-plugin-vue-inspector?tab=readme-ov-file#supported-editors) of the corresponding Vite plugin.
:::

### App Switcher

The [App Switcher](https://github.com/it-at-m/appswitcher-server) is a feature accessible from the app bar in the frontend.

While developing, this is especially useful to access useful development tools tied to the local Docker stack.
This includes the KeyCloak management UI, pgAdmin to check the application database and a possibility to open Vue DevTools in a separate browser tab.

::: tip Tip
The configuration in the `application.yml` file (inside the `appswitcher-server` directory of the stack) can be modified to include additional tools required for your specific project setup.
:::

### Linting and Code Formatting

- **Frontend**: [Prettier](https://prettier.io/) and [ESLint](https://eslint.org/) are used for linting and code formatting.
- **Backend**: For linting and code formatting, [Spotless](https://github.com/diffplug/spotless) and [PMD](https://pmd.github.io/) are utilized.

### Flyway

For database migrations, [Flyway](https://documentation.red-gate.com/flyway/getting-started-with-flyway) is employed. It runs at application startup. The following commands can also be used directly:

- Clean database: `mvn flyway:clean`
- Apply migrations: `mvn flyway:migrate`
- Reset and migrate: `mvn flyway:clean flyway:migrate`
