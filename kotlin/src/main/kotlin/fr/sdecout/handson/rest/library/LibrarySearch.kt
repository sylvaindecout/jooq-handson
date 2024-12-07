package fr.sdecout.handson.rest.library

import fr.sdecout.handson.rest.author.AuthorId
import fr.sdecout.handson.rest.book.Isbn

interface LibrarySearch {
    fun searchLibrariesClosestTo(postalCode: PostalCode): List<LibrarySearchResponseItem>
    fun searchLibrariesWithBookAvailable(isbn: Isbn): List<LibrarySearchResponseItem>
    fun countLibrariesWithBooksBy(author: AuthorId): Long
}
