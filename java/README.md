# <img src="../doc/images/logo_java.png" width="40px"> jOOQ hands-on

[![Tests](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml) [![Gitmoji](https://img.shields.io/badge/gitmoji-%20%F0%9F%98%9C%20%F0%9F%98%8D-FFDD67.svg)](https://gitmoji.dev)

## Usage

* Run tests: `./mvnw test`
* Start test DB: `cd .. && docker compose up -d`
* Start server: `./mvnw spring-boot:run`
* Populate test DB: `docker exec -i jooq-handson-database psql postgresql://user:user123@localhost:5432/handson < ../database/init_data.sql`
* Use REST API: [OpenAPI specification](../openapi.yml)

## Steps

1. [Basic commands](src/main/java/fr/sdecout/handson/persistence/library/DbLibraryAdapter.java)
2. [Queries from single tables](src/main/java/fr/sdecout/handson/persistence/library/DbLibraryAdapter.java)
3. [Queries from several tables](src/main/java/fr/sdecout/handson/persistence/library/DbLibraryAdapter.java)
4. [Nested queries](src/main/java/fr/sdecout/handson/persistence/book/DbBookAdapter.java)
5. [Batch operations](src/main/java/fr/sdecout/handson/persistence/book/DbBookAdapter.java)
6. [Code generation](src/main/resources/db/changelog/db.changelog-master.yaml)
7. [Type converters](src/main/java/fr/sdecout/handson/persistence/converters/IsbnConverter.java)
8. [Supporting specifics with Testcontainers](src/main/java/fr/sdecout/handson/rest/shared/AddressField.java)

## Context

More info in [parent directory](../README.md).
