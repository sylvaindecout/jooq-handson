package fr.sdecout.handson.rest.library

import java.util.*

@JvmInline
value class LibraryId(val value: String) {

    init {
        require(value.isNotBlank()) { "Library ID must not be blank" }
        require(value.length < 37) { "Library ID must not have more than 36 characters" }
    }

    override fun toString() = value

    companion object {
        fun next(): LibraryId = LibraryId(UUID.randomUUID().toString())
    }

}
