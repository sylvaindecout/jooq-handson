package fr.sdecout.handson.rest.library

import fr.sdecout.handson.rest.book.Isbn

fun interface BookCollectionUpdate {
    fun addBook(libraryId: LibraryId, isbn: Isbn)
}
