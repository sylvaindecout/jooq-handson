package fr.sdecout.handson.rest.book

fun interface BookAccess {
    fun findBook(isbn: Isbn): BookResponse?
}
