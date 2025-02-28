package fr.sdecout.handson.rest.author

import jakarta.validation.constraints.NotBlank

data class AuthorSearchResponseItem(
    @field:NotBlank val id: String?,
    @field:NotBlank val lastName: String?,
    @field:NotBlank val firstName: String?,
)
