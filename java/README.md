# <img src="../doc/images/logo_java.png" width="40px"> jOOQ hands-on

[![Tests](https://github.com/sylvaindecout/jooq-handson/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/jooq-handson/actions/workflows/maven.yml) [![Gitmoji](https://img.shields.io/badge/gitmoji-%20%F0%9F%98%9C%20%F0%9F%98%8D-FFDD67.svg)](https://gitmoji.dev)

## Steps

[Introduction](../doc/intro.md)

1. [Basic commands](src/main/java/fr/sdecout/handson/persistence/library/DbLibraryAdapter.java)
2. [Queries from single tables](src/main/java/fr/sdecout/handson/persistence/library/DbLibraryAdapter.java)
3. [Queries from several tables](src/main/java/fr/sdecout/handson/persistence/library/DbLibraryAdapter.java)
4. [Nested queries](src/main/java/fr/sdecout/handson/persistence/book/DbBookAdapter.java)
5. [Batch operations](src/main/java/fr/sdecout/handson/persistence/book/DbBookAdapter.java)
6. [Code generation](src/main/java/fr/sdecout/handson/rest/shared/BookField.java)
7. [Type converters](src/main/java/fr/sdecout/handson/persistence/converters/IsbnConverter.java)
8. [Supporting specifics with Testcontainers](src/main/java/fr/sdecout/handson/rest/shared/AddressField.java)

[Conclusion](../doc/conclusion.md)

> 💡 Feeling stuck?
> * Do you have a clear idea of the [DB schema](../README.md#db-schema)?
> * Did you start the test DB so that you can experiment with SQL queries? (cf. [Usage](#usage))
> * Have you checked out the link to jOOQ documentation provided in the step instructions?

## Usage

* Run tests: `./mvnw test`
* Use application on local environment
  1. Start test DB: `cd .. && docker compose up -d`
  2. Start server: `./mvnw spring-boot:run`
  3. Populate test DB: `docker exec -i jooq-handson-database psql postgresql://user:user123@localhost:5432/handson < ../database/init_data.sql`
  4. Use REST API: [OpenAPI specification](../openapi.yml)

## Troubleshooting

* You use IntelliJ as your IDE, and it does not recognize the project:
  * Right-click `pom.xml` file and select "Add as Maven Project".

* You use IntelliJ as your IDE, and it does find source code generated by jOOQ:
  * Right-click `src/generated` directory and select "Mark directory as Generated Sources Root". 

* Tests fail with the following error:
  ```
  Failed to load ApplicationContext for [WebMergedContextConfiguration@...]
  java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@...]
    [...]
  Caused by: java.lang.IllegalStateException: Could not find a valid Docker environment. Please see logs and check configuration
    [...]
  ```
  * Check that Docker is up and running
  * If you use an alternative to Docker, check [General Container runtime requirements for Testcontainers](https://java.testcontainers.org/supported_docker_environment/)

* When you start test DB, it fails with the following error:
  ```
  unable to get image 'jooq-handson-database': Cannot connect to the Docker daemon at unix:///Users/sylvaindecout/.docker/run/docker.sock. Is the docker daemon running?
  ```
  * Check that Docker is up and running

## Context

More info in [parent directory](../README.md).
