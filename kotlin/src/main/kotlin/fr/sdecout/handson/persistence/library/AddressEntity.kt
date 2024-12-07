package fr.sdecout.handson.persistence.library

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Deprecated("Replace with jOOQ")
@Embeddable
data class AddressEntity(
    @Column(name = "ADDRESS_LINE_1", nullable = false) val line1: String,
    @Column(name = "ADDRESS_LINE_2") val line2: String? = null,
    @Column(nullable = false) val postalCode: String,
    @Column(nullable = false) val city: String,
)
