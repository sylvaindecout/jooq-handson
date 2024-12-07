package fr.sdecout.handson.persistence.book

import fr.sdecout.handson.rest.author.AuthorId
import fr.sdecout.handson.rest.book.*
import fr.sdecout.handson.persistence.author.AuthorRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@Transactional
class DbBookAdapter(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
) : BookAccess, BookSearch, BookUpdate {

    override fun findBook(isbn: Isbn): BookResponse? = bookRepository.findByIdOrNull(isbn.compressedValue)
        ?.toBookResponse()

    override fun searchBooks(hint: String): List<BookSearchResponseItem> = bookRepository
        .findByTitleLikeIgnoringCase("%$hint%")
        .map { it.toBookSearchResponseItem() }

    override fun save(isbn: Isbn, title: String, authors: Collection<AuthorId>) {
        BookEntity(
            isbn = isbn.compressedValue,
            title = title,
            authors = authors.map { authorRepository.getReferenceById(it.value) }.toMutableList()
        ).let { bookRepository.save(it) }
    }

}
