package fr.sdecout.handson.persistence.author

import org.springframework.data.jpa.repository.JpaRepository

@Deprecated("Replace with jOOQ")
interface AuthorRepository : JpaRepository<AuthorEntity, String> {
    fun findByLastNameLikeIgnoringCase(hint: String): List<AuthorEntity>
}
