package fr.sdecout.handson.rest.shared

import jakarta.validation.constraints.NotBlank

data class AddressField(
    @field:NotBlank val line1: String?,
    val line2: String? = null,
    @field:NotBlank val postalCode: String?,
    @field:NotBlank val city: String?,
)
