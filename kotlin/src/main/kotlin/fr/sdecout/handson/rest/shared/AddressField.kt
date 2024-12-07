package fr.sdecout.handson.rest.shared

import jakarta.validation.constraints.NotBlank

/**
 * # TODO: Step 8
 *
 * While all RDBMS come with standard SQL, they also introduce specifics.
 * For instance, [PostgreSQL](https://www.postgresql.org) introduces a JSONB type that is more performant than native
 * JSON and has specific operators and indexing capabilities.
 *
 * Uncomment `004.add-library-address-as-jsonb.yaml` migration script in `db.changelog-master.yaml` in order to add a
 * JSONB field.
 * This breaks the build!
 * The reason lies in the process used to generate jOOQ sources (cf.
 * [`source-generation.md`](https://mermaid.live/edit#pako:eNqlkk1PwkAQhv_KZE6alEqhhXZjOCCJ8eTBm-llaYeyke7W6VYFwn93CwXRyInsZSbzzjzzsVvMTE4osKb3hnRGMyULlmWqASrJVmWqktpCQZpYWspB1vB4dO7nfDepTcMZ1X8z5o1a7dXT1kh1G2fKLHAxlzdx7MEgCD0IwsCDvh_ctnEAbSwBq2JpwSwONQS8WFcXZtODJGNy7F-sfN6CjoIDudebTFygy24jpPMrmnjSyiq5UhtyIKizJZXyX96Z8Ed1Hfu4cOh2DQs25YU2Tl3UVQs7V7g17bvsql6Y4XRqAc8fxJ-sLF0_wos11elCOdWWzdo1dOFipuqI6GHBKkdhuSEPS-JSti5uW0GK1o1AKQpn5pLfUkz1zuW4X_FqTHlMY9MUSxQLuaqd11S5G6_76CeJgxE_mEZbFEG0L4Fii18oBnHoR8NklATjUeSeh2sUYeiPwyQZj5JwFA-TONx5uNkj-348jnbfzLIMTw),
 * which uses an [in-memory database](https://h2database.com).
 *
 * Replacing it with the actual RDBMS is still possible by running a dedicated container, which using
 * [Testcontainers](https://testcontainers.com) makes way easier.
 * Unfortunately, the official Maven / Gradle plugins do not include this feature.
 * However, it can be integrated into the build specifically.
 * Still, it can be noted that
 * [a dedicated Maven plugin](https://github.com/testcontainers/testcontainers-jooq-codegen-maven-plugin) was recently released to address this specific problematic.
 *
 * @see <a href="https://blog.jooq.org/using-testcontainers-to-generate-jooq-code/">Using Testcontainers to Generate jOOQ Code</a>
 * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/json-functions/">JSON functions</a>
 */
data class AddressField(
    @field:NotBlank val line1: String?,
    val line2: String? = null,
    @field:NotBlank val postalCode: String?,
    @field:NotBlank val city: String?,
)
