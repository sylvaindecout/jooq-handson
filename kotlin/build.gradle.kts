import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Nullability.NOT_NULL

val archunitVersion = "1.4.0"
val jooqLiquibaseVersion = "3.19.18"
val kotlinLoggingVersion = "7.0.4"
val picocliVersion = "4.7.6"

plugins {
  kotlin("jvm") version "2.1.10"
  kotlin("plugin.spring") version "2.1.10"
  id("org.jetbrains.kotlin.plugin.jpa") version "2.1.10"
  id("org.springframework.boot") version "3.4.3"
  id("io.spring.dependency-management") version "1.1.7"
  id("org.jooq.jooq-codegen-gradle") version "3.19.18"
  id("org.liquibase.gradle") version "3.0.1"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation("io.github.oshai", "kotlin-logging", kotlinLoggingVersion)

  implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")

  implementation("org.springframework.boot", "spring-boot-starter-web")
  implementation("org.springframework.boot", "spring-boot-starter-validation")
  implementation("org.springframework.boot", "spring-boot-starter-actuator")
  implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
  implementation("org.springframework.boot", "spring-boot-starter-jooq")
  implementation("org.jooq", "jooq-kotlin")
  implementation("org.jooq", "jooq-jackson-extensions", dependencyManagement.importedProperties["jooq.version"] as String)

  runtimeOnly("org.postgresql", "postgresql")
  implementation("org.liquibase", "liquibase-core")

  liquibaseRuntime("org.liquibase", "liquibase-core")
  liquibaseRuntime ("info.picocli", "picocli", picocliVersion)
  liquibaseRuntime("org.postgresql", "postgresql")

  jooqCodegen("org.postgresql", "postgresql")
  jooqCodegen("org.testcontainers", "postgresql")

  testImplementation("org.springframework.boot", "spring-boot-starter-test") {
    exclude("org.junit.vintage", "junit-vintage-engine")
  }
  testImplementation("org.springframework.boot", "spring-boot-testcontainers")
  testImplementation("org.testcontainers", "postgresql")

  testImplementation("com.tngtech.archunit", "archunit", archunitVersion)
  testImplementation("com.tngtech.archunit", "archunit-junit5", archunitVersion)
}

buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath("org.testcontainers", "postgresql", "1.20.4")
    classpath("org.liquibase", "liquibase-core", "4.29.2")
  }
}

kotlin {
  compilerOptions {
    jvmTarget.set(JVM_21)
    languageVersion.set(KOTLIN_2_0)
  }
}

tasks {
  test {
    useJUnitPlatform()
  }
}

springBoot {
  mainClass.set("fr.sdecout.handson.AppKt")
}

jooq {
  version = dependencyManagement.importedProperties["jooq.version"] as String

  configuration {
    logging = Logging.WARN
    jdbc {
      driver = "org.postgresql.Driver"
      url = System.getProperty("jooq.codegen.jdbc.url")
      user = System.getProperty("jooq.codegen.jdbc.username")
      password = System.getProperty("jooq.codegen.jdbc.password")
    }
    generator {
      name = "org.jooq.codegen.KotlinGenerator"
      database {
        excludes = "DATABASECHANGELOG | DATABASECHANGELOGLOCK"
        inputSchema = "public"
        withForcedTypes(
          ForcedType()
            .withUserType("fr.sdecout.handson.rest.shared.Isbn")
            .withConverter("fr.sdecout.handson.persistence.converters.IsbnConverter")
            .withIncludeExpression("BOOK.ISBN|BOOK_AUTHOR.BOOK|LIBRARY_BOOK.BOOK")
            .withNullability(NOT_NULL)
        )
      }
      generate {
        isKotlinNotNullPojoAttributes = true
        isKotlinNotNullRecordAttributes = true
        isKotlinNotNullInterfaceAttributes = true
      }
      target {
        packageName = "fr.sdecout.handson.persistence.jooq"
        directory = "src/generated/jooq"
      }
    }
  }
}

liquibase {
  activities.register("main") {
    val allArgs = mapOf<String, String>(
      "changelogFile" to "src/main/resources/db/changelog/db.changelog-master.yaml",
      "driver" to "org.postgresql.Driver",
      "logLevel" to "debug",
      "url" to System.getProperty("jooq.codegen.jdbc.url"),
      "username" to System.getProperty("jooq.codegen.jdbc.username"),
      "password" to System.getProperty("jooq.codegen.jdbc.password"),
    )
    arguments = allArgs
  }
}

tasks.named("compileKotlin") {
  dependsOn(tasks.named("jooqCodegen"))
}

tasks.register("startDatabase") {
  doLast {
    org.testcontainers.containers.PostgreSQLContainer("postgres:15")
      .withDatabaseName("handson")
      .withUsername("user")
      .withPassword("user123")
      .apply { start() }
      .apply {
        System.setProperty("jooq.codegen.jdbc.url", jdbcUrl)
        System.setProperty("jooq.codegen.jdbc.username", username)
        System.setProperty("jooq.codegen.jdbc.password", password)
        System.setProperty("testcontainer.containerid", containerId)
        System.setProperty("testcontainer.imageName", dockerImageName)
      }
  }
  finalizedBy("migrateDatabaseSchema")
}

tasks.register("migrateDatabaseSchema") {
  dependsOn("update")
}

tasks.register("stopDatabase") {
  doLast {
    val containerId = System.getProperty("testcontainer.containerid")
    val imageName = System.getProperty("testcontainer.imageName")
    org.testcontainers.utility.ResourceReaper
      .instance()
      .stopAndRemoveContainer(containerId, imageName);
  }
}

tasks.named("jooqCodegen") {
  dependsOn("startDatabase")
  finalizedBy("stopDatabase")
}
