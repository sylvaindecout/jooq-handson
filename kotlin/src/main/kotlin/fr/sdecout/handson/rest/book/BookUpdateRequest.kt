package fr.sdecout.handson.rest.book

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class BookUpdateRequest(
    @field:NotBlank val title: String?,
    @field:Valid @field:NotEmpty val authors: List<String>?,
)
