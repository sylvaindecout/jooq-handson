package fr.sdecout.handson.rest.shared;

import jakarta.validation.constraints.NotBlank;

/**
 * <h1>TODO: Step 7</h1>
 * <p>
 * While all RDBMS come with standard SQL, they also introduce specifics.
 * For instance, <a href="https://www.postgresql.org">PostgreSQL</a> introduces a JSONB type that is more performant
 * than native JSON and has specific operators and indexing capabilities.
 * </p>
 * <p>
 * Uncomment <code>003.add-library-address-as-jsonb.yaml</code> migration script in
 * <code>db.changelog-master.yaml</code> in order to add a JSONB field.
 * This breaks the build!
 * The reason lies in the process used to generate jOOQ sources (cf. <code>source-generation.md</code>), which uses an
 * <a href="https://h2database.com">in-memory database</a>.
 * </p>
 * <p>
 * Replacing it with the actual RDBMS is still possible by running a dedicated container, which using
 * <a href="https://testcontainers.com">Testcontainers</a> makes way easier.
 * Unfortunately, the official Maven / Gradle plugins do not include this feature.
 * However, it can be integrated into the build specifically.
 * Still, it can be noted that
 * <a href="https://github.com/testcontainers/testcontainers-jooq-codegen-maven-plugin">a dedicated Maven plugin</a>
 * was recently released to address this specific problematic.
 * </p>
 *
 * @see <a href="https://blog.jooq.org/using-testcontainers-to-generate-jooq-code/">Using Testcontainers to Generate jOOQ Code</a>
 */
public record AddressField(
        @NotBlank String line1,
        String line2,
        @NotBlank String postalCode,
        @NotBlank String city
) {
}
