package fr.sdecout.handson.rest.library

import fr.sdecout.handson.rest.shared.AddressField

fun interface LibraryCreation {
    fun addLibrary(name: String, address: AddressField): LibraryId
}
