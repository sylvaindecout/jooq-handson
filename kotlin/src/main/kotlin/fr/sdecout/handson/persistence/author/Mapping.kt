package fr.sdecout.handson.persistence.author

import fr.sdecout.handson.persistence.book.toBookField
import fr.sdecout.handson.rest.author.AuthorResponse
import fr.sdecout.handson.rest.author.AuthorSearchResponseItem

fun AuthorEntity.toAuthorResponse() = AuthorResponse(
    id = this.id,
    lastName = this.lastName,
    firstName = this.firstName,
    publishedBooks = this.publishedBooks!!.map { it.toBookField() },
)

fun AuthorEntity.toAuthorSearchResponseItem() = AuthorSearchResponseItem(
    id = this.id,
    lastName = this.lastName,
    firstName = this.firstName,
)
