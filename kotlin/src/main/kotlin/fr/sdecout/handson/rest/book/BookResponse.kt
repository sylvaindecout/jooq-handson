package fr.sdecout.handson.rest.book

import fr.sdecout.handson.rest.shared.AuthorField
import fr.sdecout.handson.rest.shared.LibraryField
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class BookResponse(
    @field:NotBlank val isbn: String?,
    @field:NotBlank val title: String?,
    @field:Valid @field:NotEmpty val authors: List<AuthorField>?,
    @field:Valid val availability: List<LibraryField>?,
)
