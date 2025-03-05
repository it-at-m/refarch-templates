# Develop

We include various development tools to simplify the development process and make development more comfortable and error-resistant.
Those tools are further explained below.

::: danger IMPORTANT
Please make sure you worked through the corresponding [Getting Started](./getting-started#documentation) instructions before proceeding.
:::

## Technologies

Key technologies used in the templates include:

### Docker

[Docker](https://www.docker.com/) is used to run a local development stack including all necessary services.

::: danger IMPORTANT
If you are developing locally, you will need to have Docker installed on your system and the stack running at all times.
Also make sure you have `kubernetes.docker.internal` in your hosts file. This should normally be done automatically by the Docker installation.
:::

Inside the `stack` folder, you will find a `docker-compose.yml` file that will spin up everything needed for local development.
You can spin up the stack by using the integrated Docker features of your favorite IDE, by using a dedicated Docker UI
or by executing the command `docker compose up -d` from within the `stack` folder.

Stack components (as Docker Images):

- [Keycloak](https://www.keycloak.org/): Keycloak instance as a local SSO provider
- [Keycloak Migration](https://mayope.github.io/keycloakmigration/): Migration tool to set up the local SSO provider by executing scripts upon startup, configured via `.yml` files in `stack/keycloak/migration`
- [PostgreSQL](https://www.postgresql.org): Database instance for application data
- [pgAdmin](https://www.pgadmin.org/): Database management UI pre-configured to connect to the local PostgreSQL instance
- [API Gateway](https://refarch.oss.muenchen.de/gateway): API gateway of the RefArch, configured via [environment variables](https://refarch.oss.muenchen.de/gateway#configuration) in `docker-compose.yml`
- [Appswitcher Server](https://github.com/it-at-m/appswitcher-server): Server component to access local development tools via the frontend UI

::: info Information
Some tools provide local Browser-based UIs. We encourage you to use the UI provided by App Switcher to open them.
:::

### Vite

[Vite](https://vite.dev/) is used as the build tool for JavaScript-based projects, along with the testing framework [Vitest](https://vitest.dev/).

The following npm scripts are provided for working with those tools:

- Start Vite development server: `npm run dev`
- Run Vitest test execution: `npm run test`
- Build the Vite project (for production): `npm run build`

::: danger IMPORTANT
When you experience a refresh loop while developing with the Vite development server, please make sure to re-login via the running API Gateway.
To avoid this problem, we recommend accessing the development server using the API Gateway as a proxy.
Benefits like Hot Module Reloading (HMR) still work when tunneling.
:::

### Maven

[Maven](https://maven.apache.org/) is used as the build tool for Java-based projects.

The following maven commands are useful when working locally:

- Compile the application and execute tests: `mvn clean verify`  
  (add `-DskipTests` to skip test execution)
- Run the application: `mvn spring-boot:run -Dspring-boot.run.profiles=local`

::: info Information
Instead of compiling and running the application using the commands above, you can also use the features of your IDE directly.
:::

By default, two different Spring profiles are provided to run the application:

- `local`: Uses the local Docker stack to run the application and provides useful logging information while developing
- `no-security`: Disables all security mechanisms

### Component libraries

We use the following component libraries to accelerate our frontend development and standardize the look and feel of our applications:

- Development of standalone web applications and SPAs: [Vuetify](https://vuetifyjs.com/en/)
- WebComponent development for integration with [official Munich website](https://www.muenchen.de/): [PatternLab](https://it-at-m.github.io/muc-patternlab-vue/?path=/docs/getting-started--docs)

### Code Quality

#### Frontend / WebComponent

[Prettier](https://prettier.io/) and [ESLint](https://eslint.org/) are used for linting and code formatting JavaScript, TypeScript and Vue-based code.
Additionally, [vue-tsc](https://github.com/vuejs/language-tools/tree/master/packages/tsc) is used for running type-checking when working with TypeScript.

You can run those tools in combination by using the following npm scripts:

- Lint your source code: `npm run lint`
- Autofix issues: `npm run fix`

::: info Information
Not all issues are auto-fixable, so you still might have some manual work to do after running the command.
:::

The tools are configured through the respective configuration files

- Prettier: `.prettierrc.json` (points to a [centralized configuration](https://github.com/it-at-m/itm-prettier-codeformat))
- ESLint: `eslint.config.js` (configuration part of the templates)

#### Backend / EAI

[Spotless](https://github.com/diffplug/spotless), [PMD](https://pmd.github.io/) and [SpotBugs](https://spotbugs.github.io/) are used for code formatting and linting Java-based code.
Additionally, [find-sec-bugs](https://github.com/find-sec-bugs/find-sec-bugs) is used to check for vulnerabilities inside your code.

Those tools are configured inside the `pom.xml` files and automatically run when executing the respective Maven phases. (e.g. `mvn verify`)
Alternatively you can also run the custom maven goals provided by those plugins:

- Run Spotless formatting check: `mvn spotless:check`
- Run Spotless formatting autofix: `mvn spotless:apply`
- Run PMD lint check: `mvn pmd:check`
- Run PMD CPD ([Copy/Paste Detector](https://pmd.github.io/pmd/pmd_userdocs_cpd.html)) check: `mvn pmd:cpd-check`
- Run SpotBugs lint check: `mvn spotbugs:check`  
  (**Note**: Requires project compilation before execution when code changes were made)

::: info Information
Issues reported by the PMD and SpotBugs are currently not auto-fixable so you still have some manual work to do.
:::

The tools are configured through the respective configuration files or configuration sections inside the `pom.xml`

- Spotless: `pom.xml` and using a [centralized configuration](https://github.com/it-at-m/itm-java-codeformat)
- PMD: `pom.xml` and using centralized configuration (more information in [RefArch documentation](https://refarch.oss.muenchen.de/tools.html#pmd))
- SpotBugs: `pom.xml` and `spotbugs-exclude-rules.xml` (configuration part of the templates)

### Vue Dev Tools

The [Vue Dev Tools](https://devtools.vuejs.org/) provide useful features when developing with Vue.js. Those include checking and editing component state, debugging the [Pinia](https://pinia.vuejs.org/) store, testing client-side routing, inspecting page elements and way more.

The Vue Dev Tools are included as a development dependency inside the templates, so no further installation is required.

A useful feature is the inspection of elements, which allows you to click components of your webpage inside your Browser-rendered application and open the relevant part right in your IDE.
To make use of this feature, a few steps have to be made on your machine.

::: info Information
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

### Database Migration

[Flyway](https://documentation.red-gate.com/flyway) is used as our tool for database migration.

It runs automatically when starting the backend application.
Additionally, the following maven goals can be run manually:

- Clean database: `mvn flyway:clean -Dflyway.cleanDisabled=false`
- Apply migrations: `mvn flyway:migrate`
- Reset and migrate: `mvn flyway:clean flyway:migrate -Dflyway.cleanDisabled=false`

To maintain your migration files, check the folder `db.migration` inside the `resources` folder of the Java project.
For more information about how to work with Flyway, checkout its [Getting Started guide](https://documentation.red-gate.com/flyway/getting-started-with-flyway)

### App Switcher

The [App Switcher](https://github.com/it-at-m/appswitcher-server) is a feature accessible from the app bar in the frontend.

While developing, this is especially useful to access useful development tools tied to the local Docker stack.
This includes the Keycloak management UI, pgAdmin to check the application database and a possibility to open Vue DevTools in a separate browser tab.

::: info Information
The configuration in the `application.yml` file (inside the `appswitcher-server` directory of the stack) can be modified to include additional tools required for your specific project setup.
:::

## Lifecycle Management (LCM)

[Renovate](https://docs.renovatebot.com/) is used to keep your dependencies up to date.

The templates by default make use of a centralized configuration we provide for RefArch-based projects. More information can be found in the [RefArch documentation](https://refarch.oss.muenchen.de/tools.html#renovate).

To modify the default Renovate settings, the `renovate.json5` file in the project's root directory can be edited.

::: info Information
By default Renovate creates grouped PRs for dependency bumps. This means that if a particular dependency is found in multiple package manager files, the bump will be applied to all occurrences.
If you want Renovate to create separate PRs by subfolder, add <code v-pre>"additionalBranchPrefix": "{{parentDir}}-"</code> to your Renovate configuration.
Check the official [Renovate documentation](https://docs.renovatebot.com/configuration-options/#additionalbranchprefix) for further information and configuration options.
:::

::: danger IMPORTANT
To make Renovate work, make sure that it has access to your GitHub repository.
For projects in the `it-at-m` organization, Renovate has access by default and is enabled when the configuration file is found in your repository.
However, to finish the onboarding process of Renovate, you need to open a PR for a dependency update found by Renovate through the "Dependency Dashboard" issue.
This PR then has to be merged manually once.
After that's done Renovate will start opening PRs automatically.
:::

## Pull Requests

When a pull request (PR) is created, several tools help maintain code quality:

### Code Rabbit

**Code Rabbit** is an AI-powered code reviewer that assists with PR assessments. The configuration file can be found at the root of the templates in `.coderabbit.yaml`.

Our configuration enables automatic reviews (and follow-up reviews when changes to a PR have been made). Additionally, we set CodeRabbit in "nitpicky" mode to find all of those nasty bugs.
Feel free to customize the configuration to your own needs. More information is available in the [official documentation](https://docs.coderabbit.ai/).

::: info Information
To make CodeRabbit work, make sure that it has access to your GitHub repository.
For projects in the `it-at-m` organization, CodeRabbit automatically has access and is enabled when the configuration file is found in your repository.
:::

::: danger IMPORTANT
Code Rabbit is free to use for open-source projects. If you are developing a project with no public visibility, you might need to remove the `.coderabbit.yaml` file.
:::

### CodeQL

**CodeQL** is a GitHub tool for discovering vulnerabilities and code smells in code. More details can be found [here](https://codeql.github.com/).

The template enables CodeQL for Pull Requests and configures CodeQL to only scan for Java and JavaScript/TypeScript/Vue files by default.
For further information on how to change the configuration, please check out the documentation of the related custom [GitHub workflow](https://github.com/it-at-m/.github/blob/main/workflow-templates/codeql.yaml).

::: danger IMPORTANT
If you are using Java-based projects inside your repository, you need to add those to the `java-build-path` variable pointing to the directory of the `pom.xml` files.
:::

### Dependency Review

To ensure that only dependencies with approved licenses are included, a [global check](https://github.com/it-at-m/.github/blob/main/workflow-configs/dependency_review.yaml) is implemented.
This is enabled by default when using the templates. To learn more about the Dependency Review feature itself, please check the official [GitHub documentation](https://docs.github.com/en/code-security/supply-chain-security/understanding-your-software-supply-chain/about-dependency-review).

The allowed licenses can be viewed [here](https://opensource.muenchen.de/licenses.html#integration-in-in-house-developments).

### Require PR checklist

The templates provide a workflow for validating checklist status in a PR description and the PR discussion. To merge a PR, all checklist items must be ticked off by the PR creator.

The templates by default ship with a [PR template](./organize.html#pull-request-template), which makes use of a checklist.

::: info Information
If some of the PR checklist items are not relevant for your PR, you should adjust the checklist inside the PR description to the specific PR changes.
If you want to disable the feature completely, you need to remove the file `.github/workflows/pr-checklist.yml`.
:::

::: danger IMPORTANT
This functionality conflicts with the [Finishing touches](https://docs.coderabbit.ai/finishing-touches/docstrings/) feature of CodeRabbit. That's why this feature of CodeRabbit is disabled inside its configuration file by default.
If you don't use "Require PR checklist" you can re-enable this functionality by altering the `.coderabbit.yaml` file.
:::

### GitHub Rulesets

It is recommended to review the rulesets for pushing and merging in the GitHub repository. Depending on the project's branching strategy, some branches should be protected to prevent force pushes and merging without approval.
More information about Rulesets can be found in the [official GitHub documentation](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/creating-rulesets-for-a-repository).

::: danger IMPORTANT
Note that the tools mentioned above (CodeRabbit, CodeQL, Dependency Review) are optional by default and are not required to pass when a PR should be merged.
We strongly encourage you to enable the status checks for those tools before being able to merge a PR. More information about status checks can be found [here](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/collaborating-on-repositories-with-code-quality-features/about-status-checks).
Status checks are configurable as part of the rulesets.
:::

## CI/CD Configurations

The `.github/workflows` folder contains various GitHub workflow files. Those reference centralized actions to simplify different parts of the CI/CD process.
It also helps to keep Lifecycle Management as simple as possible as no direct dependency on third-party actions exists.

More information about the centralized actions can be found in the [lhm_actions documentation](https://github.com/it-at-m/lhm_actions/blob/main/docs/actions.md).

::: danger IMPORTANT
Note that the CI/CD setup of the templates is in a Work-In-Progress state, so its subject to change in the near future.
:::

## CODEOWNERS

The **CODEOWNERS** file (found under the `.github` directory of the template) is an essential tool in version control systems like GitHub.

It specifies who is responsible for different parts of the project, ensuring that the right people are involved in code reviews.
When modifications are made to these files, the designated owners receive a review request automatically, enhancing code quality and accountability.

Each line identifies a file or directory along with the owner(s) using their GitHub usernames or team names.

::: danger IMPORTANT
Please alter the CODEOWNERS file to list project members or team names for your own project.
Otherwise, the RefArch maintainers have to approve all your code changes.
You definitely don't want that, as we are super nitpicky when it comes to code quality. ;)
:::

To learn more about the CODEOWNERS file, please check the official [GitHub documentation](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-code-owners).
