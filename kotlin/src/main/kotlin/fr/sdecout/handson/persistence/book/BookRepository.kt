package fr.sdecout.handson.persistence.book

import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<BookEntity, String> {
    fun findByTitleLikeIgnoringCase(hint: String): List<BookEntity>
}
