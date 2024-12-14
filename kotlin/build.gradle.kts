import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
import org.jooq.meta.jaxb.Logging

val archunitVersion = "1.3.0"
val jooqLiquibaseVersion = "3.19.10"
val kotlinLoggingVersion = "7.0.0"

//buildscript {
//  dependencies {
//    classpath("org.postgresql", "postgresql")
//    classpath("org.testcontainers", "postgresql")
//  }
//}

plugins {
  kotlin("jvm") version "2.1.0"
  kotlin("plugin.spring") version "2.1.0"
  id("org.jetbrains.kotlin.plugin.jpa") version "2.1.0"
  id("org.springframework.boot") version "3.4.0"
  id("io.spring.dependency-management") version "1.1.6"
  id("org.jooq.jooq-codegen-gradle") version "3.19.15"
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

  // jooqCodegen("org.jooq", "jooq-meta-extensions-liquibase", jooqLiquibaseVersion)
  // jooqCodegen("org.liquibase", "liquibase-core")

  jooqCodegen("org.postgresql", "postgresql")
  jooqCodegen("org.testcontainers", "postgresql")
  jooqCodegen("org.jooq","jooq-codegen", dependencyManagement.importedProperties["jooq.version"])
  jooqCodegen("org.jooq","jooq-meta-extensions", dependencyManagement.importedProperties["jooq.version"])

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
        excludes = "DATABASECHANGELOG | DATABASECHANGELOGLOCK"
        inputSchema = "public"
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

tasks.register("startDatabase") {
//  dependencies {
//    implementation("org.postgresql", "postgresql")
//    implementation("org.testcontainers", "postgresql")
//  }
  doLast {
//    val db = org.testcontainers.containers.PostgreSQLContainer("mysql:latest")
//      .withDatabaseName("handson")
//      .withUsername("user")
//      .withPassword("user123")
//    db.start()

    println(">>> startDatabase > Starting testcontainer")

    // See https://www.jooq.org/doc/latest/manual/code-generation/codegen-system-properties/
//    System.setProperty("jooq.codegen.jdbc.url", db.getJdbcUrl())
//    System.setProperty("jooq.codegen.jdbc.username", db.getUsername())
//    System.setProperty("jooq.codegen.jdbc.password", db.getPassword())
//    System.setProperty("testcontainer.containerid", db.getContainerId())
//    System.setProperty("testcontainer.imageName", db.getDockerImageName())
    System.setProperty("jooq.codegen.jdbc.url", "jdbc:postgresql://localhost:5432/handson")
    System.setProperty("jooq.codegen.jdbc.username", "user")
    System.setProperty("jooq.codegen.jdbc.password", "user123")
  }
}

tasks.register("stopDatabase") {
  doLast {
    val containerId = System.getProperty("testcontainer.containerid")
    val imageName = System.getProperty("testcontainer.imageName")

    println(">>> stopDatabase > Stopping testcontainer:  $containerId - $imageName")
//    org.testcontainers.utility.ResourceReaper
//      .instance()
//      .stopAndRemoveContainer(containerId, imageName);
  }
}

tasks.named("jooqCodegen") {
  dependsOn(tasks.named("startDatabase"))
  finalizedBy(tasks.named("stopDatabase"))
  doFirst {
    println(">>> jooqCodegen > This is executed first during the execution phase.")
  }
  doLast {
    println(">>> jooqCodegen > This is executed last during the execution phase.")
  }
  println(">>> jooqCodegen > This is executed during the configuration phase as well, because :testBoth is used in the build.")
}
