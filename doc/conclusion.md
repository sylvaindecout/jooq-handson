# Wrap-up

## Step back

Let's take a step back and check how the situation has changed (you may refer to [Solution branch](https://github.com/sylvaindecout/jooq-handson/tree/Solution) in case you doubt your implementation).

![Before/after comparison](images/before_after.svg)

* Spring Data repositories have been replaced with `DSLContext`.
* 😟 For most basic queries, Spring Data repositories makes it unnecessary to write most common queries. Now, we need to write them explicitly.
* 😊 For complex queries, we use pseudo-SQL instead of a methods with names based on a convention. This allows for more flexibility.
* 😊 Testing queries is easy and feels natural.
* 😁 Entities have been replaced with generated code. No need to maintain them anymore!
* 😁 No more complex annotations.
* 😁 No cyclic dependencies, no lazy loading. Everything is made necessarily explicit.

ORMs came with the need to improve on JDBC. So they got rid of SQL.

SQL was never the problem. We love SQL. What we actually needed was type safety.

## Legacy remediation

Most who have worked on legacy applications will relate to the fact that the part of the code that uses the ORM is often one that tends to get especially hard to maintain:

* It is a prime source of bugs, one that is prone to many regressions.
* It is often poorly tested, if at all.
* It often mixes with the business logic. In some cases, it is even completely coupled with it.
* Too many developers only have a superficial grasp of how to use it properly.

That makes it pretty hard to refactor, because any change is likely to result in many uncontrolled impacts.

On the contrary, as we have seen, jOOQ makes source code more expressive and more explicit.
This in itself is a start in sanitizing legacy code.

Besides, migrating to jOOQ can be used as the occasion to regain control of your code base.
You can address some underlying problems by questioning the interface with the DB directly on the level of SQL, without the bias of the object paradigm and its constraints.

Finally, the fact that jOOQ is easy to integrate and can live alongside JPA makes it especially convenient for a gradual migration.

To go into this topic in further detail, [a series of articles about jOOQ and legacy code is available on Shodo's blog](https://shodo.io/jooq-et-le-code-legacy-1).

## Side notes

* Code generation is not mandatory. But do you really want to do it manually, and even more so have to maintain it?
* jOOQ is not only about typesafe SQL. An active-records approach and [DAO generation](https://blog.jooq.org/to-dao-or-not-to-dao/) are also available.
  It makes sense for CRUD, but that is not what gives relevance to jOOQ.
* jOOQ is free for recent versions of most open source DBs. But you have to pay if you use older versions, or DBs such as Oracle or SQL Server. See the [version support matrix](https://www.jooq.org/download/support-matrix) for detailed info.
* Of course, jOOQ works without Spring. The main difference is that you need to rely on jOOQ to deal with transactions.

## What now?

You should now have everything you need to integrate jOOQ in your real-life application without a big bang effect! 🚀

## Going one step further

* [JOOQ, Joy of SQL](https://www.youtube.com/watch?v=fW80PwtNJAM) is a great presentation of jOOQ (also available [in French](https://www.youtube.com/watch?v=5m_oE0iPJJE)).
* You can check out alternatives to jOOQ along with some insight on [this repository (Java persistence frameworks comparison)](https://github.com/bwajtr/java-persistence-frameworks-comparison).
* A series of articles (in French) about jOOQ and legacy code is available on [Shodo's blog](https://shodo.io/jooq-et-le-code-legacy-1).
