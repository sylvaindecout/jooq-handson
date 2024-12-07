package fr.sdecout.handson.rest.shared

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

/**
 * # TODO: Step 6
 *
 * Uncomment `003.add-book-release-year.yaml` migration script in `db.changelog-master.yaml` in order to add a
 * new field.
 *
 * Note that the field is not available at once. You first need to build once.
 *
 * Check the content of `:heavy_plus_sign: Integrate jOOQ` commit in order to understand how code generation is
 * automated with [gradle-jooq-plugin](https://github.com/etiennestuder/gradle-jooq-plugin).
 * You can have a look at `source-generation.md` to understand the steps of the generation.
 */
data class BookField(
    @field:NotNull @field:ValidIsbn val isbn: String?,
    @field:NotBlank val title: String?,
    @field:Valid @field:NotEmpty val authors: List<AuthorField>?,
)
