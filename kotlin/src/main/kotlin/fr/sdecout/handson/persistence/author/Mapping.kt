package fr.sdecout.handson.persistence.author

import fr.sdecout.handson.rest.author.AuthorResponse
import fr.sdecout.handson.rest.author.AuthorSearchResponseItem
import fr.sdecout.handson.persistence.book.toBookField
import fr.sdecout.handson.rest.shared.AuthorField

fun AuthorEntity.toAuthorResponse() = AuthorResponse(
    id = this.id,
    lastName = this.lastName,
    firstName = this.firstName,
    publishedBooks = this.publishedBooks!!.map { it.toBookField() },
)

fun AuthorEntity.toAuthorSearchResponseItem() = AuthorSearchResponseItem(
    author = this.toAuthorField()
)

private fun AuthorEntity.toAuthorField() = AuthorField(
    id = this.id,
    lastName = this.lastName,
    firstName = this.firstName,
)
