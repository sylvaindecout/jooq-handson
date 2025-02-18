# <img src="../doc/images/logo_kotlin.png" width="40px"> jOOQ hands-on

[![Tests](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml) [![Gitmoji](https://img.shields.io/badge/gitmoji-%20%F0%9F%98%9C%20%F0%9F%98%8D-FFDD67.svg)](https://gitmoji.dev)

## Usage

* Run tests: `./gradlew test`
* Start test DB: `cd .. && docker compose up -d`
* Start server: `./gradlew bootRun`
* Populate test DB: `docker exec -i jooq-handson-database psql postgresql://user:user123@localhost:5432/handson < ../database/init_data.sql`
* Use REST API: [OpenAPI specification](../openapi.yml)

## Context

More info in [parent directory](../README.md).
