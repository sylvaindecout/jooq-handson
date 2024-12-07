package fr.sdecout.handson.rest.library

import fr.sdecout.handson.rest.shared.Isbn

fun interface BookCollectionUpdate {
    fun addBook(libraryId: LibraryId, isbn: Isbn)
}
