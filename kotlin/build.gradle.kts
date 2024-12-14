import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.integration.commandline.CommandLineUtils
import liquibase.resource.CompositeResourceAccessor
import liquibase.resource.FileSystemResourceAccessor
import liquibase.resource.ResourceAccessor
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
import org.jooq.meta.jaxb.Logging
import java.util.*


val archunitVersion = "1.3.0"
val jooqLiquibaseVersion = "3.19.10"
val kotlinLoggingVersion = "7.0.0"

plugins {
  kotlin("jvm") version "2.1.0"
  kotlin("plugin.spring") version "2.1.0"
  id("org.jetbrains.kotlin.plugin.jpa") version "2.1.0"
  id("org.springframework.boot") version "3.4.0"
  id("io.spring.dependency-management") version "1.1.6"
  id("org.jooq.jooq-codegen-gradle") version "3.19.15"
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

  runtimeOnly("org.postgresql", "postgresql")
  implementation("org.liquibase", "liquibase-core")

  // jooqCodegen("org.jooq", "jooq-meta-extensions-liquibase", jooqLiquibaseVersion)
  // jooqCodegen("org.liquibase", "liquibase-core")

  liquibaseRuntime("org.liquibase", "liquibase-core")
  liquibaseRuntime ("info.picocli", "picocli", "4.7.6") // FIXME: variabilize version
  liquibaseRuntime("org.postgresql", "postgresql")
  // liquibaseRuntime ("org.liquibase", "liquibase-groovy-dsl", "3.0.2") // FIXME: remove?

  jooqCodegen("org.postgresql", "postgresql")
  jooqCodegen("org.testcontainers", "postgresql")
//  jooqCodegen("org.jooq","jooq-codegen", dependencyManagement.importedProperties["jooq.version"])
//  jooqCodegen("org.jooq","jooq-meta-extensions", dependencyManagement.importedProperties["jooq.version"])

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
    classpath("org.testcontainers", "postgresql", "1.20.4") // FIXME
    classpath("org.liquibase", "liquibase-gradle-plugin", "3.0.1") {
      exclude(group="org.liquibase", module="liquibase-core")
    }
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
//  activities {
//    closureOf<NamedDomainObjectContainer<Activity>> {
//      create("main", closureOf<Activity> {
//        arguments = mapOf(
//          "driver" to "org.h2.Driver",
//          "changeLogFile" to "src/main/resources/db/changelog.xml",
//          "url" to "jdbc:h2:mem:test",
//          "logLevel" to "debug"
//        )
//      })
//    }
//  }
  activities.register("main") {
      val allArgs = mapOf<String, String>(
        "changelogFile" to "src/main/resources/db/changelog/db.changelog-master.yaml",
        "driver" to "org.postgresql.Driver",
        "logLevel" to "debug",
        "url" to "jdbc:postgresql://localhost:5432/handson",
        "user" to "user",
        "username" to "user",
        "password" to "user123",


//        "url" to System.getProperty("jooq.codegen.jdbc.url"),
//        "user" to System.getProperty("jooq.codegen.jdbc.username"),
//        "password" to System.getProperty("jooq.codegen.jdbc.password"),
//        "changeLogDirectory" to "${buildFile.parent}/src/main/resources", // ${projectDir}
//        "changelog-directory" to "${buildFile.parent}/src/main/resources", // ${projectDir}
//        "searchPath" to "src/main/resources",
//        "search-path" to "src/main/resources",
//        "changeLogFile" to "/db/changelog/db.changelog-master.yaml",
//        "liquibase.command.update.changelogFile" to "/db/changelog/db.changelog-master.yaml",
//        "liquibase.command.changelogFile" to "/db/changelog/db.changelog-master.yaml",
//        "changelog-file" to "/db/changelog/db.changelog-master.yaml",
//        "--changelog-file" to "/db/changelog/db.changelog-master.yaml",
//        "--changelog-file" to "/db/changelog/db.changelog-master.yaml",
//        "logLevel" to "debug",
//        "help" to "",
//        "changelogFile" to "src/main/resources/db/changelog/db.changelog-master.yaml",
        )
//      println(">>> this is me! " + allArgs.toList().joinToString(separator = "\n"))
//      changelogParameters = allArgs
      arguments = allArgs
    // runList = "main" // FIXME: unnecessary?
  }
}

tasks.named("compileKotlin") {
  dependsOn(tasks.named("jooqCodegen"))
}

//tasks.named("update") {
//  doFirst {
//    println(">>> update > Starting")
//  }
//  doLast {
//    println(">>> update > Disposing")
//  }
//}

tasks.register("startDatabase") {
  doLast {
    println(">>> startDatabase > Starting testcontainer")
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
  finalizedBy("update") // liquibase
}

//tasks.register("migrateSchema") {
//  doLast {
//    this.url = url
//    this.username = user
//    this.password = password
//    this.driver = "" //your driver
//    this.outputFile = "" // The path in which the script have to be flushed.
//    val fsOpener: FileSystemResourceAccessor = liquibase.resource.FileSystemResourceAccessor()
//    val clOpener: CommandLineResourceAccessor = CommandLineResourceAccessor(javaClass.classLoader)
//    val fileOpener: CompositeResourceAccessor =
//      liquibase.resource.CompositeResourceAccessor(*kotlin.arrayOf<ResourceAccessor>(fsOpener, clOpener))
//
//    val database: Database = CommandLineUtils.createDatabaseObject(
//      fileOpener,
//      this.url,
//      this.username,
//      this.password,
//      this.driver,
//      this.defaultCatalogName,
//      this.defaultSchemaName,
//      this.outputDefaultCatalog.toBoolean(),
//      this.outputDefaultSchema.toBoolean(),
//      this.databaseClass,
//      this.driverPropertiesFile,
//      this.propertyProviderClass,
//      this.liquibaseCatalogName,
//      this.liquibaseSchemaName,
//      this.databaseChangeLogTableName,
//      this.databaseChangeLogLockTableName
//    )
//
//
//    val liquibase: Liquibase = liquibase.Liquibase(d, null, database)
//    liquibase.update(liquibase.Contexts(this.contexts), liquibase.LabelExpression(this.labels), getOutputWriter())
//
//  }
//}

tasks.register("stopDatabase") {
  doLast {
    val containerId = System.getProperty("testcontainer.containerid")
    val imageName = System.getProperty("testcontainer.imageName")
    println(">>> stopDatabase > Stopping testcontainer:  $containerId - $imageName")
    org.testcontainers.utility.ResourceReaper
      .instance()
      .stopAndRemoveContainer(containerId, imageName);
  }
}

tasks.named("jooqCodegen") {
  dependsOn("startDatabase")
  finalizedBy("stopDatabase")
}

/*
            <plugin>
                <groupId>org.liquibase</groupId>
                <artifactId>liquibase-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>init-db</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>update</goal>
                        </goals>
                        <configuration>
                            <url>${jooq.codegen.jdbc.url}</url>
                            <username>${jooq.codegen.jdbc.username}</username>
                            <password>${jooq.codegen.jdbc.password}</password>
                            <changeLogDirectory>${basedir}/src/main/resources</changeLogDirectory>
                            <changeLogFile>/db/changelog/db.changelog-main.yaml</changeLogFile>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
 */
