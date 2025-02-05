# Develop

## Docker Stack



## CI/CD Configurations
### TBD

## Codeowners

A CODEOWNERS file is a special file used in version control systems like GitHub to define who is responsible for specific parts of a project. 
It allows teams to designate individuals or teams as "owners" for different files or directories in the repository.

When a change is made to a file or directory covered by the CODEOWNERS file, the designated owners will automatically be requested for review.
This helps ensure that the right people are involved in the review process, improving code quality and accountability.

### How It Works

- **Location**: The CODEOWNERS file is located  in the `.github/` directory.
- **Syntax**: Each line in the file specifies a path and the corresponding owner(s) using their GitHub usernames or team names.


## Pull Request Tooling
If a pull Request gets created, there are differnt tools that will help to kepp good code quality

### Code Rabbit
Code Rabbit ia an AI-Powered code reviewer for pull requests. 
The configuration file can be found at the root of the project inside the `.coderabbit.yaml` file.
More Informations about Code Rabbit can be found [here](https://docs.coderabbit.ai/)

### CodeQL
CodeQL is a Github Tool that helps discover vulnerabilityies. 
To use it, you need to adjust the file inside `.github/workflows/codeql.yml`, where you need to add your java projects to the `java-build-path` variable.
More Informations can be found [here](https://codeql.github.com/)

### Dependencie Review
To check, that only dependencies with allowed licenses are contained, there is a [global check](https://github.com/it-at-m/.github/blob/main/workflow-configs/dependency_review.yaml).
The allowed licenses can be viewed [here](https://opensource.muenchen.de/de/licenses.html#einbindung-in-eigenentwicklungen)

### Github Rules
We recommend checking the rulesets for pushing and merging inside the github repository.
Depending on your project and Branching strategy, some branches should be protected to deny force pushes and Merging withouth approve.
The Setting can be changed under `Settings > Rules > Rulesets`.

## LCM Tooling

To keep our LCM up-to-date we are using [Renovate](https://docs.renovatebot.com/). 
The Bot is globaly configured with [custom settings](https://github.com/it-at-m/.github/tree/main/renovate-configs)
and refarch [specific settings](https://github.com/it-at-m/refarch/blob/main/refarch-tools/refarch-renovate/refarch-renovate-config.json5).
these configurations contain settings, so that for each directory a branch for an dependecie upgrade will be created.
e.g. If Two projects contain `spotless-maven-plugin` there will be two branches, one for each project.

If you want to change the default renovate-bot settings, you can change the `renovate.json5` file inside the root of your project
and add or change settings. 

::: danger IMPORTANT
Normally Renovate will Auto-Merge created MRs that have been approved. 
To activate that behavior, the first PR needs to be merged manually
:::

## Technologies

In the following, the note worthy Technologies that are used for the Templates will be explained briefly.

### Vite
For our Build Tool, [Vite](https://vite.dev/) is used with the corresponding Test Framework [Vitest](https://vitest.dev/)

### Patternlab
When Using the refarch-webcomponent for development and integration inside the official website of munich, webcomponents should use [Patternlab](https://it-at-m.github.io/muc-patternlab-vue/?path=/docs/getting-started--docs) for UI-Components 

### Vue Dev Tools
The [Vue Dev tools](https://devtools.vuejs.org/) are available inside the browser console and via the icon on the bottom of the screen.

### App Switcher
The [App swichter](https://github.com/it-at-m/appswitcher-server/pkgs/container/appswitcher-server) can be found at the app-bar int he frontend. 
To Change the icons inside, you can add them inside the Docker-compose file configuration

### Linting and Code-formatter
#### Frontend
For linting and Code-Formatting [Prettier](https://prettier.io/) and [EsLint](https://eslint.org/) is beeing used.
#### Backend
For Linting and code-Formatting [Spotless](https://github.com/diffplug/spotless) and [PMD](https://pmd.github.io/) is beeing used

### Flyway
For Database Migrations [Flyway](https://documentation.red-gate.com/flyway/getting-started-with-flyway) is beeing used. It will run on the startup of the application. If you want to run it directly you can use the commands
```
 - Clean database: mvn flyway:clean
 - Apply migrations: mvn flyway:migrate
 - Reset and migrate: mvn flyway:clean flyway:migrate
```