package fr.sdecout.handson.persistence.book

import fr.sdecout.handson.persistence.author.AuthorEntity
import fr.sdecout.handson.persistence.library.toLibraryField
import fr.sdecout.handson.rest.book.BookResponse
import fr.sdecout.handson.rest.book.BookSearchResponseItem
import fr.sdecout.handson.rest.shared.AuthorField
import fr.sdecout.handson.rest.shared.BookField
import fr.sdecout.handson.rest.shared.Isbn

fun BookEntity.toBookResponse() = BookResponse(
    isbn = Isbn(this.isbn).formattedValue,
    title = this.title,
    authors = this.authors.map { it.toAuthorField() },
    availability = this.availability!!.map { it.toLibraryField() },
)

fun BookEntity.toBookSearchResponseItem() = BookSearchResponseItem(
    isbn = Isbn(this.isbn).formattedValue,
    title = this.title,
    authors = this.authors.map { it.toAuthorField() },
)

fun BookEntity.toBookField() = BookField(
    isbn = Isbn(this.isbn).formattedValue,
    title = this.title,
    authors = this.authors.map { it.toAuthorField() },
)

private fun AuthorEntity.toAuthorField() = AuthorField(
    id = this.id,
    lastName = this.lastName,
    firstName = this.firstName,
)
