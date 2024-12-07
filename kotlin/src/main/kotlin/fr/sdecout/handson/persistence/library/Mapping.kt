package fr.sdecout.handson.persistence.library

import fr.sdecout.handson.rest.library.LibraryResponse
import fr.sdecout.handson.rest.library.LibrarySearchResponseItem
import fr.sdecout.handson.rest.shared.AddressField
import fr.sdecout.handson.rest.shared.LibraryField

fun LibraryEntity.toLibraryResponse() = LibraryResponse(
    id = this.id,
    name = this.name,
    address = this.address.toAddressField(),
)

fun LibraryEntity.toLibrarySearchResponseItem() = LibrarySearchResponseItem(
    library = this.toLibraryField()
)

fun LibraryEntity.toLibraryField() = LibraryField(
    id = this.id,
    name = this.name,
    address = this.address.toAddressField(),
)

private fun AddressEntity.toAddressField() = AddressField(
    line1 = this.line1,
    line2 = this.line2,
    postalCode = this.postalCode,
    city = this.city,
)

fun AddressField.toAddressEntity() = AddressEntity(
    line1 = this.line1!!,
    line2 = this.line2,
    postalCode = this.postalCode!!,
    city = this.city!!,
)
