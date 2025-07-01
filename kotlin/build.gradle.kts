import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property

val archunitVersion = "1.4.0"
val jooqLiquibaseVersion = "3.19.22"
val kotlinLoggingVersion = "7.0.4"
val liquibaseVersion = "4.31.0"

plugins {
  kotlin("jvm") version "2.1.21"
  kotlin("plugin.spring") version "2.1.21"
  id("org.jetbrains.kotlin.plugin.jpa") version "2.2.0"
  id("org.springframework.boot") version "3.4.4"
  id("io.spring.dependency-management") version "1.1.7"
  id("org.jooq.jooq-codegen-gradle") version "3.19.22"
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
  implementation("org.liquibase", "liquibase-core", liquibaseVersion)

  jooqCodegen("org.liquibase", "liquibase-core")
  jooqCodegen("org.jooq", "jooq-meta-extensions-liquibase", jooqLiquibaseVersion)

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
        name = "org.jooq.meta.extensions.liquibase.LiquibaseDatabase"
        withProperties(
          Property().withKey("rootPath").withValue("$projectDir/src/main/resources"),
          Property().withKey("scripts").withValue("/db/changelog/db.changelog-master.yaml"),
          Property().withKey("includeLiquibaseTables").withValue("false")
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

tasks.named("compileKotlin") {
  dependsOn(tasks.named("jooqCodegen"))
}
