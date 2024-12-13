# Dependencies

## Core Spring Boot Dependencies

- **spring-boot-starter-web**:  
  Provides the essential components for building web applications with Spring MVC.

- **spring-boot-starter-data-jpa**:  
  Simplifies the integration of JPA (Java Persistence API) into Spring applications,
  making it easier to work with relational databases.

- **spring-boot-starter-data-rest**:  
  Enables the creation of RESTful APIs from Spring Data repositories, allowing for easy
  exposure of data as REST endpoints.

- **spring-boot-starter-actuator**:  
  Adds production-ready features to your application, such as monitoring and
  management over REST endpoints.

## Caching

- **spring-boot-starter-cache**:  
  Provides support for caching in Spring applications, facilitating performance
  improvements by storing frequently accessed data temporarily.

- **caffeine**:  
  A high-performance caching library that integrates with Spring’s caching
  abstraction to enhance data retrieval speeds.

## Security

- **spring-boot-starter-security**:  
  Provides comprehensive security services for your application, including
  authentication and authorization capabilities.

- **spring-security-oauth2-client**:  
  Adds support for OAuth 2.0 client functionalities, allowing your application to
  connect with external OAuth 2.0 providers.

- **spring-security-oauth2-resource-server**:  
  Enables your application to act as an OAuth 2.0 resource server, protecting your
  REST APIs.

- **spring-security-oauth2-jose**:  
  Provides support for JSON Web Tokens (JWT) in OAuth 2.0 flows, facilitating
  secure token management.

## Data Management

- **postgresql**:  
  Allows the application to communicate with PostgreSQL databases.

- **hibernate-core**:  
  The core ORM (Object-Relational Mapping) framework that facilitates database
  operations via Java objects.

- **hibernate-jpamodelgen**:  
  Enables JPA model generation at compile time, improving type safety in JPA
  queries.

- **flyway-core**:  
  A tool for managing database migrations, ensuring your database schema is
  always up-to-date.

- **flyway-database-postgresql**:  
  Provides the necessary PostgreSQL functionality for Flyway migrations.

## JSON Processing

- **spring-hateoas**:  
  Adds support for Hypermedia as the Engine of Application State (HATEOAS),
  enhancing REST API responses with links.

- **jackson-databind**:  
  A powerful library for converting Java objects to and from JSON.

- **jackson-datatype-jsr310**:  
  Adds support for Java 8 Date and Time API types in Jackson.

- **json-path**:  
  A library for querying JSON structures, enabling complex data retrieval
  from JSON objects.

## Validation

- **jakarta.validation-api**:  
  Provides the necessary API for bean validation, ensuring that data conforms
  to defined constraints.

- **hibernate-validator**:  
  The reference implementation of the Jakarta Bean Validation API, used for
  validating Java objects.

## Testing Dependencies

- **spring-boot-starter-test**:  
  A comprehensive starter for testing Spring applications, including JUnit,
  AssertJ, and Mockito.

- **spring-security-test**:  
  Provides utilities for testing security features in Spring applications.

- **spring-boot-testcontainers**:  
  Integrates Testcontainers to support integration testing with real databases.

- **junit-jupiter-api**:  
  The API for writing tests in JUnit 5, supporting modern testing practices.

- **junit-jupiter-engine**:  
  The engine that runs JUnit 5 tests.

## Monitoring and Metrics

- **micrometer-tracing**:  
  Adds tracing capabilities to your application, allowing for better
  observability.

- **micrometer-tracing-bridge-brave**:  
  Integrates Brave tracing library with Micrometer.

- **logstash-logback-encoder**:  
  A Logback encoder for sending structured logs to Logstash, enabling better
  log management.

- **micrometer-registry-prometheus**:  
  Integrates Prometheus with Micrometer for metrics collection and monitoring.

## Miscellaneous

- **spring-data-rest-hal-explorer**:  
  Provides an interactive HAL browser for Spring Data REST applications,
  making it easier to explore API endpoints.

- **commons-collections4**:  
  Provides additional data structures and algorithms for Java collections.

- **commons-csv**:  
  A library for reading and writing CSV files, simplifying CSV data manipulation.

- **commons-io**:  
  Contains utility classes for working with IO operations, enhancing file
  handling capabilities.

- **commons-lang3**:  
  Offers helper utilities for the Java core classes, improving development
  efficiency.

- **commons-text**:  
  Provides additional text processing capabilities, enhancing string
  manipulation.

- **commons-validator**:  
  A library for providing validation routines, ensuring data integrity.

- **spring-boot-configuration-processor**:  
  A processor for generating metadata for application properties, improving
  IDE support and documentation.

## Code Quality and Annotations

- **lombok**:  
  A library that reduces boilerplate code in Java by providing annotations
  for automatic generation of getters, setters, and more.

- **mapstruct**:  
  A code generator that simplifies the implementation of bean mapping,
  improving data transfer between different object models.

- **spotbugs-annotations**:  
  Provides annotations that help with static analysis of your code, allowing
  developers to identify potential bugs early.

## Build Configuration

The build configuration section defines plugins that enhance the Maven build
process:

- **spring-boot-maven-plugin**:  
  Facilitates packaging and running Spring Boot applications, enabling easy
  creation of executable JARs.

- **maven-compiler-plugin**:  
  Configures the Java compiler by specifying the source and target Java
  versions. It also sets up annotation processors for libraries like Lombok
  and MapStruct, allowing for code generation and enhancements during
  compilation.

- **maven-surefire-plugin**:  
  Runs the tests in your project, providing fine control over the test
  execution environment, including encoding settings.

- **jacoco-maven-plugin**:  
  Measures code coverage during tests, generating reports that help
  identify untested parts of the codebase.

- **spotless-maven-plugin**:  
  Ensures code formatting consistency by applying specified style rules to
  Java files, using configurations from an Eclipse formatter file.

- **maven-pmd-plugin**:  
  Executes static code analysis to identify potential issues in your code,
  applying custom rulesets and providing reports on code quality.

- **spotbugs-maven-plugin**:  
  Performs static analysis to detect bugs in Java code, integrating security
  checks with the FindSecBugs plugin for enhanced security assessments.

- **flyway-maven-plugin**:  
  Manages database migrations, allowing for version control of database
  schemas and ensuring that the database is always in an expected state.

This configuration collectively ensures that the project is built, tested, and
maintained efficiently, promoting code quality and adherence to best practices.
