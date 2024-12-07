package fr.sdecout.handson.rest.author

fun interface AuthorAccess {
    fun findAuthor(id: AuthorId): AuthorResponse?
}
