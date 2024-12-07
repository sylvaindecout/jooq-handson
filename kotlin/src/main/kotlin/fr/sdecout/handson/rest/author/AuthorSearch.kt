package fr.sdecout.handson.rest.author

fun interface AuthorSearch {
    fun searchAuthors(hint: String): List<AuthorSearchResponseItem>
}
