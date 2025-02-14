# Development

## CI/CD Configurations

TBD

## CODEOWNERS

The **CODEOWNERS** file is an essential tool in version control systems like GitHub. It specifies who is responsible for different parts of the project, ensuring that the right people are involved in code reviews.

### How It Works

- **Location**: The CODEOWNERS file resides in the `.github/` directory.
- **Syntax**: Each line identifies a file or directory along with the owner(s) using their GitHub usernames or team names.

When modifications are made to these files, the designated owners receive a review request automatically, enhancing code quality and accountability.

## Pull Request Tooling

When a pull request (PR) is created, several tools help maintain code quality:

### Code Rabbit

**Code Rabbit** is an AI-powered code reviewer that assists with PR assessments. The configuration file can be found at the root of the project in `.coderabbit.yaml`. More information is available [here](https://docs.coderabbit.ai/).

### CodeQL

**CodeQL** is a GitHub tool for discovering vulnerabilities in code. To use it, update the `.github/workflows/codeql.yml` file by adding Java projects to the `java-build-path` variable. More details can be found [here](https://codeql.github.com/).

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

The [App Switcher](https://github.com/it-at-m/appswitcher-server/pkgs/container/appswitcher-server) is accessible from the app bar in the frontend. Icons can be updated by modifying the Docker Compose file configuration.

### Linting and Code Formatting

- **Frontend**: [Prettier](https://prettier.io/) and [ESLint](https://eslint.org/) are used for linting and code formatting.
- **Backend**: For linting and code formatting, [Spotless](https://github.com/diffplug/spotless) and [PMD](https://pmd.github.io/) are utilized.

### Flyway

For database migrations, [Flyway](https://documentation.red-gate.com/flyway/getting-started-with-flyway) is employed. It runs at application startup. The following commands can also be used directly:

- Clean database: `mvn flyway:clean`
- Apply migrations: `mvn flyway:migrate`
- Reset and migrate: `mvn flyway:clean flyway:migrate`
