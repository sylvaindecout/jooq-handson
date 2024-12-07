package fr.sdecout.handson.rest.author

fun interface AuthorCreation {
    fun addAuthor(lastName: String, firstName: String): AuthorId
}
