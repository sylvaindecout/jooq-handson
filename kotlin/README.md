# <img src="../doc/images/logo_kotlin.png" width="40px"> jOOQ hands-on

[![Tests](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml) [![Gitmoji](https://img.shields.io/badge/gitmoji-%20%F0%9F%98%9C%20%F0%9F%98%8D-FFDD67.svg)](https://gitmoji.dev)

## Usage

* Run tests: `./gradlew test`
* Start test DB: `cd .. && docker compose up -d`
* Start server: `./gradlew bootRun`
* Use REST API: [OpenAPI specification](../openapi.yml)

# Steps

1. [Basic commands](src/main/kotlin/fr/sdecout/handson/persistence/library/DbLibraryAdapter.kt)
2. [Queries from single tables](src/main/kotlin/fr/sdecout/handson/persistence/library/DbLibraryAdapter.kt)
3. [Queries from several tables](src/main/kotlin/fr/sdecout/handson/persistence/library/DbLibraryAdapter.kt)
4. [Nested queries](src/main/kotlin/fr/sdecout/handson/persistence/book/DbBookAdapter.kt)
5. [Batch operations](src/main/kotlin/fr/sdecout/handson/persistence/book/DbBookAdapter.kt)
6. [Type converters](src/main/kotlin/fr/sdecout/handson/persistence/converters/IsbnConverter.kt)
7. [Supporting specifics with Testcontainers](src/main/kotlin/fr/sdecout/handson/rest/shared/AddressField.kt)

## Context

More info in [parent directory](../README.md).
