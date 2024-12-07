package fr.sdecout.handson.rest.shared

import jakarta.validation.constraints.NotBlank

data class AuthorField(
    @field:NotBlank val id: String?,
    @field:NotBlank val lastName: String?,
    @field:NotBlank val firstName: String?,
)
