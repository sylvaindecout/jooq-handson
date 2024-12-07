package fr.sdecout.handson.rest.book

import fr.sdecout.handson.rest.author.AuthorId

fun interface BookUpdate {
    fun save(isbn: Isbn, title: String, authors: Collection<AuthorId>)
}
