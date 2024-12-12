### Spring Boot Dependencies
- **spring-boot-starter-data-rest**: Provides RESTful APIs for Spring Data repositories to expose data entities over HTTP.
- **spring-boot-starter-data-jpa**: Integrates Spring Data JPA for simplified database access using the Java Persistence API (JPA).
- **spring-boot-starter-web**: Enables building web applications with Spring MVC, RESTful services, and embedded web servers.
- **spring-boot-starter-actuator**: Adds production-ready features to monitor and manage your application via HTTP endpoints.

### Caching Dependencies
- **spring-boot-starter-cache**: Provides caching support for Spring applications to enhance performance by minimizing database access.
- **caffeine**: A high-performance caching library for Java that offers in-memory caching capabilities.

### Spring Security Dependencies
- **spring-boot-starter-security**: Integrates Spring Security into a Spring Boot application to secure web applications and REST APIs.
- **spring-security-oauth2-client**: Provides support for OAuth 2.0 client authentication in Spring applications.
- **spring-security-oauth2-resource-server**: Enables Spring applications to act as OAuth 2.0 resource servers for protected APIs.
- **spring-security-oauth2-jose**: Supports JSON Web Tokens (JWT) for OAuth 2.0 in Spring Security.

### JSON and Related Dependencies
- **spring-hateoas**: Adds support for creating hypermedia-driven REST APIs using HATEOAS (Hypermedia as the Engine of Application State).
- **jackson-databind**: Provides data-binding capabilities for converting Java objects to/from JSON.
- **jackson-datatype-jsr310**: Adds support for Java 8 Date and Time API types in Jackson.
- **json-path**: A library for querying JSON documents using a path syntax similar to XPath.

### Database Dependencies
- **postgresql**: The PostgreSQL JDBC driver for connecting Java applications to a PostgreSQL database.
- **hibernate-core**: The core ORM framework for Java that provides mechanisms for object-relational mapping.
- **hibernate-jpamodelgen**: Generates type-safe JPA metamodel classes for Hibernate.

### Other Dependencies
- **httpclient5**: An HTTP client library for making requests and handling responses in Java.
- **lombok**: A library that reduces boilerplate code in Java by automatically generating common methods like getters and setters (provided scope means it's not included in the final artifact).
- **mapstruct**: A code generator that simplifies the implementation of mappings between Java bean types.
- **spotbugs-annotations**: Provides annotations for static analysis to detect potential bugs in Java code.

### Validation Dependencies
- **jakarta.validation-api**: The API for Java Bean Validation (JSR 380) that allows for defining validation constraints.
- **hibernate-validator**: The reference implementation of the Jakarta Bean Validation API.

### Testing Dependencies
- **spring-boot-starter-test**: Provides testing support for Spring Boot applications with libraries like JUnit, Mockito, and Spring Test.
- **spring-security-test**: Adds testing support for Spring Security components and configurations.
- **spring-boot-testcontainers**: Integrates Testcontainers with Spring Boot for integration testing with Docker containers.
- **junit-jupiter**: The JUnit 5 API for writing unit tests in a modern way.
- **junit-jupiter-engine**: The JUnit 5 engine for executing tests written with the JUnit 5 API.
