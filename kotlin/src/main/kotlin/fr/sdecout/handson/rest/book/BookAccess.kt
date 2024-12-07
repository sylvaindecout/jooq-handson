package fr.sdecout.handson.rest.book

import fr.sdecout.handson.rest.shared.Isbn

fun interface BookAccess {
    fun findBook(isbn: Isbn): BookResponse?
}
