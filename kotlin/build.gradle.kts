import nu.studer.gradle.jooq.JooqEdition
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Nullability.NOT_NULL
import org.jooq.meta.jaxb.Property

val archunitVersion = "1.3.0"
val jooqLiquibaseVersion = "3.19.10"
val kotlinLoggingVersion = "7.0.0"

plugins {
  kotlin("jvm") version "2.1.0"
  kotlin("plugin.spring") version "2.1.0"
  id("org.jetbrains.kotlin.plugin.jpa") version "2.1.0"
  id("org.springframework.boot") version "3.4.0"
  id("io.spring.dependency-management") version "1.1.6"
  id("nu.studer.jooq") version "9.0"
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

  runtimeOnly("org.postgresql", "postgresql")
  implementation("org.liquibase", "liquibase-core")

  jooqGenerator("org.jooq", "jooq-meta-extensions-liquibase", jooqLiquibaseVersion)
  jooqGenerator("org.liquibase", "liquibase-core")

  testImplementation("org.springframework.boot", "spring-boot-starter-test") {
    exclude("org.junit.vintage", "junit-vintage-engine")
  }
  testImplementation("org.springframework.boot", "spring-boot-testcontainers")
  testImplementation("org.testcontainers", "postgresql")

  testImplementation("com.tngtech.archunit", "archunit", archunitVersion)
  testImplementation("com.tngtech.archunit", "archunit-junit5", archunitVersion)
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
  version.set(dependencyManagement.importedProperties["jooq.version"])
  edition.set(JooqEdition.OSS)

  configurations {
    create("main") {
      generateSchemaSourceOnCompilation.set(true)

      jooqConfiguration.apply {
        logging = Logging.WARN
        generator.apply {
          name = "org.jooq.codegen.KotlinGenerator"
          database.apply {
            name = "org.jooq.meta.extensions.liquibase.LiquibaseDatabase"
            withProperties(
              Property().withKey("rootPath").withValue("$projectDir/src/main/resources"),
              Property().withKey("scripts").withValue("/db/changelog/db.changelog-master.yaml"),
              Property().withKey("includeLiquibaseTables").withValue("false")
            )
            withForcedTypes(
              ForcedType()
                .withUserType("fr.sdecout.handson.rest.shared.Isbn")
                .withConverter("fr.sdecout.handson.persistence.converters.IsbnConverter")
                .withIncludeExpression("BOOK.ISBN|BOOK_AUTHOR.BOOK|LIBRARY_BOOK.BOOK")
                .withNullability(NOT_NULL)
            )
          }
          generate.apply {
            isKotlinNotNullPojoAttributes = true
            isKotlinNotNullRecordAttributes = true
            isKotlinNotNullInterfaceAttributes = true
          }
          target.apply {
            packageName = "fr.sdecout.handson.persistence.jooq"
            directory = "src/generated/jooq"
          }
        }
      }
    }
  }
}
