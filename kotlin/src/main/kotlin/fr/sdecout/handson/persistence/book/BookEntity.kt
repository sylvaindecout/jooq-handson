package fr.sdecout.handson.persistence.book

import fr.sdecout.handson.persistence.author.AuthorEntity
import fr.sdecout.handson.persistence.library.LibraryEntity
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Deprecated("Replace with jOOQ")
@Entity
@Table(name = "BOOK")
data class BookEntity(

    @Id @Column(nullable = false) val isbn: String,

    @Column(nullable = false) val title: String,

    @ManyToMany
    @JoinTable(
        name = "BOOK_AUTHOR",
        joinColumns = [JoinColumn(name = "BOOK", referencedColumnName = "ISBN")],
        inverseJoinColumns = [JoinColumn(name = "AUTHOR", referencedColumnName = "ID")]
    )
    val authors: MutableList<AuthorEntity>,

    @ManyToMany(fetch = LAZY, mappedBy = "availableBooks")
    var availability: MutableList<LibraryEntity>? = null

) {
    init {
        require(authors.isNotEmpty()) { "A book must have at least one author" }
    }
}
