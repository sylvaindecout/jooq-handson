package fr.sdecout.handson.rest.shared

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LibraryField(
    @field:NotBlank val id: String?,
    @field:NotBlank val name: String?,
    @field:NotNull @field:Valid val address: AddressField?,
)
