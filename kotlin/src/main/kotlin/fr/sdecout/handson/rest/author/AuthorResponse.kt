package fr.sdecout.handson.rest.author

import fr.sdecout.handson.rest.shared.BookField
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

data class AuthorResponse(
    @field:NotBlank val id: String?,
    @field:NotBlank val lastName: String?,
    @field:NotBlank val firstName: String?,
    @field:Valid val publishedBooks: List<BookField>?,
)
