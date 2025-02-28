package fr.sdecout.handson.rest.book

import fr.sdecout.handson.rest.shared.AuthorField
import fr.sdecout.handson.rest.shared.ValidIsbn
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class BookSearchResponseItem(
    @field:NotNull @field:ValidIsbn val isbn: String?,
    @field:NotBlank val title: String?,
    @field:Valid @field:NotEmpty val authors: List<AuthorField>?,
)
