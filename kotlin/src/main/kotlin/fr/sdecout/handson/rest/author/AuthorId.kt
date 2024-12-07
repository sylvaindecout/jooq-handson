package fr.sdecout.handson.rest.author

import java.util.*

@JvmInline
value class AuthorId(val value: String) {

    init {
        require(value.isNotBlank()) { "Author ID must not be blank" }
        require(value.length < 37) { "Author ID must not have more than 36 characters" }
    }

    override fun toString() = value

    companion object {
        fun next(): AuthorId = AuthorId(UUID.randomUUID().toString())
    }

}
