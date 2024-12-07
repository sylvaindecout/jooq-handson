# jOOQ hands-on

[![Tests](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml) [![Tests](https://github.com/sylvaindecout/jooq-handson/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/jooq-handson/actions/workflows/maven.yml) [![Gitmoji](https://img.shields.io/badge/gitmoji-%20%F0%9F%98%9C%20%F0%9F%98%8D-FFDD67.svg)](https://gitmoji.dev)

<div style="display: flex; max-width: 830px; justify-content: center">

[![Java/Maven](doc/images/penguin_java.png)](java/README.md)
<picture><img alt="Choose wisely" src="doc/images/penguin.png"></picture>
[![Kotlin/Gradle](doc/images/penguin_kotlin.png)](kotlin/README.md)
</div>

## Domain

![Stock image](doc/images/books-1281581_830.jpg)

* Libraries have lots of books available
* Books have been written by 1 or more authors

## Test data

![Test data](doc/images/test-data-set.svg)

## Architecture

![Application layers](doc/images/architecture.svg)

## DB schema

![DB schema](doc/images/db_schema.png)

## Troubleshooting

* You use IntelliJ as your IDE, and it does not recognize the projects:
  * For Java, right-click `pom.xml` file and select "Add as Maven Project".
  * For Kotlin, right-click `build.gradle.kts` file and select "Link Gradle Project".

* Tests fail with the following error:
  ```
  Failed to load ApplicationContext for [WebMergedContextConfiguration@...]
  java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@...]
    [...]
  Caused by: java.lang.IllegalStateException: Could not find a valid Docker environment. Please see logs and check configuration
    [...]
  ```
  * Check that Docker is up and running

* When you start test DB, it fails with the following error:
  ```
  unable to get image 'jooq-handson-database': Cannot connect to the Docker daemon at unix:///Users/sylvaindecout/.docker/run/docker.sock. Is the docker daemon running?
  ```
  * Check that Docker is up and running
