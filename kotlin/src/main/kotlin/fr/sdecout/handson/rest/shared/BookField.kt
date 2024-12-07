package fr.sdecout.handson.rest.shared

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class BookField(
    @field:NotNull @field:ValidIsbn val isbn: String?,
    @field:NotBlank val title: String?,
    @field:Valid @field:NotEmpty val authors: List<AuthorField>?,
)
