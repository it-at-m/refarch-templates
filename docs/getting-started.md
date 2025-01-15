# Getting Started

Welcome to the Getting Started guide for the `refarch-templates`.
This guide aims to help you in setting up a code repository with `refarch-templates`
and to execute initial required steps for the individual templates.

The purpose of these templates is to provide developers with an out-of-the-box solution for
building full-stack applications based on the reference architecture (RefArch) of it@M.
For more information on the RefArch itself, please visit the [RefArch documentation](https://refarch.oss.muenchen.de/)

**Important**: To use the `refarch-templates` you need to have [Docker](https://www.docker.com/) installed on your system.

## Getting the templates

There are two different ways on how to get started with the templates. Which variant to use depends on whether you
want to create a completely new project or add new components to an existing code repository.

### Variant 1: Use GitHub template mechanism

When starting a completely new project on GitHub, it's recommended to use the "template repository" feature of GitHub.
The [refarch-templates repository](https://github.com/it-at-m/refarch-templates) is defined as a "template repository".
This means when you create a new repository for your project, you can choose to use `refarch-templates` as a starting point.

Further information can be found in the
[official GitHub documentation](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template).

### Variant 2: Download or clone templates

If you already have a code repository in place and just want to add new components (e.g. a new backend),
it's recommended to either download or clone the `refarch-templates` repository.
Use the following command to clone the repository now:

```bash
git clone https://github.com/it-at-m/refarch-templates.git
```

Once the templates are available locally, copy the folders you need into your own project directory.
For example, if you want to work with the backend, you should copy the `refarch-backend` folder.

**Important**: The `stack` folder must be copied to your project directory, as it contains essential components required by the templates.

Besides the code templates, `refarch-templates` provides lots of other configuration files which help to establish
best practices in project development, documentation and organisation. Copy those over as well, if you want to use them.

For further information on those topics please check out [Develop](./develop), [Document](./document) and [Organize](./organize).

## Stack

The following sections describe the included components for the local development stack.

### Prerequisites

- Docker

### Starting the stack

Inside the `stack` folder, you will find a `docker-compose.yml`
file that will spin up everything needed for local development.

You can spin up the stack by using the integrated Docker features of your favorite IDE, by using a dedicated Docker UI
or by executing the command `docker compose up` from within the `stack` folder.

**Important**: When developing locally, the stack should always be running.

### Stack components

#### Keycloak

- **Website**: <https://www.keycloak.org/>
- **Purpose**: Run a Keycloak instance as a local SSO provider

#### Keycloak Migration

- **Website**: <https://mayope.github.io/keycloakmigration/>
- **Purpose**: Migration tool to set up the local SSO provider by executing scripts upon startup
- **Configuration**: All necessary scripts are located in the
  `stack/keycloak/migration` folder.
  Extensions to Keycloak should be implemented through these scripts.

#### PostgreSQL

- **Website**: <https://www.postgresql.org>
- **Purpose**: Run a PostgreSQL database instance locally

#### pgAdmin

- **Website**: <https://www.pgadmin.org/>
- **Purpose**: Run a database management UI pre-configured to connect to the local PostgreSQL instance

#### API gateway

- **Website**: <https://refarch.oss.muenchen.de/gateway>
- **Purpose**: Launch the API gateway of the RefArch in your local environment
- **Configuration**: The API gateway behaviour can be adjusted by altering the respective environment variables
  defined in `stack/docker-compose.yml` under the key `refarch-gateway.environment`

#### Appswitcher Server

- **Repository**: <https://github.com/it-at-m/appswitcher-server>
- **Purpose**: Server component required for appswitcher-vue to access local development tools via the frontend UI
- **Configuration**: Configure the AppSwitcher within the `stack/appswitcher-server`
  folder.

## Backend & EAI

### Prerequisites

- Java 21
- Maven 3.9
- Docker (for Postgres and Keycloak)

### Configure templates

After getting the templates, you will need to make a few adjustments in the respective template directories:

1. Rename the folder you copied (e.g. from `refarch-backend` to `myapp-backend`).
2. Change the package name from `de.muenchen.refarch` to
   `de.muenchen.YourPackageName` (It's recommended to use an IDE for this
   task, as it will automatically update the imports; otherwise, you will
   need to do this manually).
3. Change the name of the main application class (e.g. from `MicroServiceApplication` to `MyAppApplication`)
4. Inside the `pom.xml`, update the `groupId`, `artifactId`, `name`, `description`, `url` and `scm`
   fields.
5. Install required dependencies by executing the command `mvn install`.

Congratulations, the Backend/EAI is now ready to use.

### Profiles

By default, the template supports two Maven profiles:

- `local`: Contains the necessary configurations for running the application.
  locally, including settings for ports, SSO, and the database
- `no-security`: Configured to operate without security measures for development.
  or testing purposes.
- `json-logging`: Switches logging from textual to JSON output (only relevant when deployed).

## Frontend & Web Components

### Prerequisites

- Node.js 22 LTS (`22.11.x` - `22.x.x`)
- Docker (for AppSwitcher)

### Configure Templates

After getting the templates, you will need to make a few adjustments in the respective template directories:

1. Rename the folder you copied (e.g. from `refarch-frontend` to `myapp-frontend`).
2. Change the artifact `name` inside the `package.json` file.
3. Generate a new `package-lock.json` file by executing the command `npm install`.

Congratulations, the Frontend/WebComponent is now ready to use.

## Next steps

If you finished the template configurations for all required components your project needs
you have now successfully set up your project.

To help you with software developing, documentation and project organisation, please check out [Develop](./develop), [Document](./document) and [Organize](./organize).
