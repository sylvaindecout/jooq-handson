package fr.sdecout.handson.rest.shared

import jakarta.validation.constraints.NotBlank

/**
 * # TODO: Step 7
 *
 * While all RDBMS come with standard SQL, they also introduce specifics.
 * For instance, [PostgreSQL](https://www.postgresql.org) introduces a JSONB type that is more performant than native
 * JSON and has specific operators and indexing capabilities.
 *
 * Uncomment `003.add-library-address-as-jsonb.yaml` migration script in `db.changelog-master.yaml` in order to add a
 * JSONB field.
 * This breaks the build!
 * The reason lies in the process used to generate jOOQ sources (cf. `source-generation.md`), which uses an
 * [in-memory database](https://h2database.com).
 *
 * Replacing it with the actual RDBMS is still possible by running a dedicated container, which using
 * [Testcontainers](https://testcontainers.com) makes way easier.
 * Unfortunately, the official Maven / Gradle plugins do not include this feature.
 * However, it can be integrated into the build specifically.
 * Still, it can be noted that
 * [a dedicated Maven plugin](https://github.com/testcontainers/testcontainers-jooq-codegen-maven-plugin) was recently released to address this specific problematic.
 *
 * @see <a href="https://blog.jooq.org/using-testcontainers-to-generate-jooq-code/">Using Testcontainers to Generate jOOQ Code</a>
 */
data class AddressField(
    @field:NotBlank val line1: String?,
    val line2: String? = null,
    @field:NotBlank val postalCode: String?,
    @field:NotBlank val city: String?,
)
