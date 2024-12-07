package fr.sdecout.handson.persistence.library

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LibraryRepository : JpaRepository<LibraryEntity, String> {

    fun findByAddress_postalCodeStartingWith(departmentId: String): List<LibraryEntity>

    fun findByavailableBooks_isbn(isbn: String): List<LibraryEntity>

    @Query("SELECT COUNT(DISTINCT l.id) FROM LibraryEntity l JOIN l.availableBooks b JOIN b.authors a WHERE a.id = :authorId")
    fun countLibrariesWithBooksBy(authorId: String): Long

}
