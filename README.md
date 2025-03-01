# jOOQ hands-on

[![Tests](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/jooq-handson/actions/workflows/gradle.yml) [![Tests](https://github.com/sylvaindecout/jooq-handson/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/jooq-handson/actions/workflows/maven.yml) [![Gitmoji](https://img.shields.io/badge/gitmoji-%20%F0%9F%98%9C%20%F0%9F%98%8D-FFDD67.svg)](https://gitmoji.dev)

<div style="display: flex; max-width: 830px; justify-content: center">
  <div style="display: flex; align-self: end; height: 50%; padding-right: 20px; padding-bottom: 0.1em; justify-content: end; align-items: end">

[<img src="doc/images/logo_java.png" width="80" alt="Java/Maven" />](java/README.md)
  </div>
  <div style="display: flex">
    <img src="doc/images/penguin.png" width="250" alt="Choose wisely" />
  </div>
  <div style="display: flex; align-self: start; height: 50%; padding-left: 15px; padding-top: 1em; justify-content: start; align-items: start">

[<img src="doc/images/logo_kotlin.png" width="80" alt="Kotlin/Gradle" />](kotlin/README.md)
  </div>
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

```mermaid
---
  config:
    class:
      hideEmptyMembersBox: true
---
classDiagram
  class LIBRARY {
    # ID
    NAME
    ADDRESS_LINE_1
    ADDRESS_LINE_2
    POSTAL_CODE
    CITY
  }
  class LIBRARY_BOOK {
    # BOOK
    # LIBRARY
  }
  class BOOK {
    # ISBN
    TITLE
  }
  class BOOK_AUTHOR {
    # BOOK
    # AUTHOR
  }
  class AUTHOR {
    # ID
    LAST_NAME
    FIRST_NAME
  }

  LIBRARY_BOOK --> LIBRARY
  LIBRARY_BOOK --> BOOK
  BOOK_AUTHOR --> BOOK
  BOOK_AUTHOR --> AUTHOR
```
