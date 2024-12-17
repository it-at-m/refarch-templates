# Getting Started

Welcome to the Getting Started guide for the refarch-templates. This guide
is divided into sections for Frontend/Web Component and Backend/EAI, as well
as a general setup and requirements applicable to all templates.

## General

To use the refarch-templates, you need to have Docker installed on your
machine. Inside the `stack` folder, you will find a `docker-compose.yml`
file that will spin up everything needed for local development. 

When developing locally, the stack should always be running. 
### Download Templates

You can download the templates or clone them directly from the GitHub
repository.
```
git clone https://github.com/it-at-m/refarch-templates.git
```
Once the templates are available locally, copy the
folders you need into your own project directory. For example, if you want
to work with the backend, you should copy the `refarch-backend` folder. 

**Important**: You also need to copy the Stack folder to your project once. 

## Stack

The stack includes:

**Keycloak**
- **Local Instance**: Set up and run a Keycloak instance locally.

**Keycloak Migration**
- **Migration Tool**: Utilize a migration tool that executes scripts upon startup.
- **Script Location**: All necessary scripts are located in the `stack/keycloak` folder. 
Extensions to Keycloak should be implemented through these scripts.

**PostgreSQL**
- **Local Instance**: Set up and run a PostgreSQL instance locally.

**PGAdmin**
- **Local Instance**: Run a pre-configured instance of PgAdmin that connects to your local PostgreSQL database.

**API Gateway**
- **Local Instance**: Launch the ITM API-gateway in your local environment.

**AppSwitcher**
- **Repository**:https://github.com/it-at-m/appswitcher-server
- **Configuration**: Configure the AppSwitcher within the `stack/appswitcher-server` folder.


## Backend & EAI

### Prerequisites

- Java 21
- Maven 3.9.7
- Docker (Postgres/Keycloak)

### Configure Templates

After copying the templates, you will need to make a few adjustments:

1. Rename the folder you copied.
2. Change the package name from `de.muenchen.refarch` to
   `de.muenchen.YourPackageName` (It's recommended to use an IDE for this
   task, as it will automatically update the imports; otherwise, you will
   need to do this manually).
3. Inside the `pom.xml`, update the `artifactId`, `groupId`, and `name`
   fields.

After Running `mvn install` the Backend/EAI is ready to use

### Profiles

By default, the template supports two profiles:

**local**
- Contains the necessary configurations for running the application
  locally, including settings for ports, SSO, and the database.

**no-security**
- Configured to operate without security measures for development
  or testing purposes.


## Frontend and Web Components

### Prerequisites

- Node.js version 20 to 22
- Docker (AppSwitcher)

### Configure Templates

After copying the templates, make the following adjustments:

1. Change the name inside the `package.json` file on line 2.
2. Generate a new `package-lock.json` file.
