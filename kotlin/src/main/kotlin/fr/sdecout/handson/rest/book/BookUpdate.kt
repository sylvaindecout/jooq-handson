package fr.sdecout.handson.rest.book

import fr.sdecout.handson.rest.author.AuthorId
import fr.sdecout.handson.rest.shared.Isbn

fun interface BookUpdate {
    fun save(isbn: Isbn, title: String, authors: Collection<AuthorId>)
}
