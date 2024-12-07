package fr.sdecout.handson.rest.library

fun interface LibraryAccess {
    fun findLibrary(id: LibraryId): LibraryResponse?
}
