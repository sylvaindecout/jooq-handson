# Getting started

## What you should expect

At the end of the hands-on, you should have everything you need to integrate [jOOQ](https://www.jooq.org) in your real-life application without a big bang effect.

## Initial source code

Let's start by checkout out the initial source code.

We have some tests annotated with `@Tag("Acceptance")` that run against the application along with a PostgreSQL DB thanks to Testcontainers.
Let's use them as a safety net in order to replace JPA with jOOQ.

## The problem with JPA

* `@Entity` annotation comes with some constraints:
  * All fields in the entities have to be mutable. Lists notably have to be defined as mutable, which is especially obvious is Kotlin.
  * Some fields in the entities have to be nullable in order to support lazy loading.
* You define one single model for all queries.
  * As a result, we need some cyclic dependencies, and lazy loading to make them work. 
    But this is not always enough, as `LibraryRepositoryTest` illustrates.
  * Lazy loading works in the context of an active session, which is not so explicit.
    Using lazy loading in the wrong place leads to the infamous `LazyInitializationException`.
  * Many-to-many relationships come with not-so-easy-to-understand combinations of annotations (`@ManyToMany` and `@JoinTable`).
* Can you predict what SQL queries are executed by the ORM, in particular when lazy loading is involved?
  Try to guess for `should find author` in `AuthorControllerTest` and check the actual queries in the logs.
  You may think you tested your queries, but depending on the context they may be completely different in production.

JPA is really convenient and straightforward for basic use cases, but it is easy to lose control as your application grows more complex, leading to hard-to-deal-with legacy code.

## Introducing jOOQ

> jOOQ **generates** Java code from your database and lets you build **type-safe** SQL queries through its **fluent API**.

Technically, jOOQ is a lib that builds upon a tool that generates source code.

It is basically a come back to JDBC, except type safety means we removed the issue that made ORMs necessary in the first place.

## Step 0

How does jOOQ integrate?

You should take a look at the commit called `:heavy_plus_sign: Integrate jOOQ` in the Git log:

* Importing `spring-boot-starter-jooq` includes all necessary libs.
* A bean is defined in `AppConfig` for some basic configuration.
* jOOQ plugin is configured for [source generation](source-generation.md).
  By default, the DB used for source generation is an [in-memory H2 database](https://h2database.com).

## Next steps

This hands-on is available in [Java/Maven](../java/README.md) and in [Kotlin/Gradle](../kotlin/README.md).

Now just choose a language and follow the TODOs! :rocket:
