import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0

val archunitVersion = "1.3.0"
val kotlinLoggingVersion = "7.0.4"
val liquibaseVersion = "4.31.0"

plugins {
  kotlin("jvm") version "2.1.10"
  kotlin("plugin.spring") version "2.1.10"
  id("org.jetbrains.kotlin.plugin.jpa") version "2.1.10"
  id("org.springframework.boot") version "3.4.2"
  id("io.spring.dependency-management") version "1.1.7"
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

  runtimeOnly("org.postgresql", "postgresql")
  implementation("org.liquibase", "liquibase-core", liquibaseVersion)

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
