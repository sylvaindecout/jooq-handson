package fr.sdecout.handson.rest.book

fun interface BookSearch {
    fun searchBooks(hint: String): List<BookSearchResponseItem>
}
