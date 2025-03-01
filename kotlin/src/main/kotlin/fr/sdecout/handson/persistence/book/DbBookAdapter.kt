package fr.sdecout.handson.persistence.book

import fr.sdecout.handson.persistence.author.AuthorRepository
import fr.sdecout.handson.rest.author.AuthorId
import fr.sdecout.handson.rest.book.*
import fr.sdecout.handson.rest.shared.Isbn
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

    /**
     * # TODO: Step 4
     *
     * At this stage, we can deal with 95% of our use cases. Now let's get a step further.
     *
     * 1-N relationships can be dealt with multiple queries, but standard SQL includes the capability to nest queries
     * with `MULTISET` function.
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/multiset-value-constructor/">MULTISET value constructor</a>
     */
    override fun searchBooks(hint: String): List<BookSearchResponseItem> = bookRepository
        .findByTitleLikeIgnoringCase("%$hint%")
        .map { it.toBookSearchResponseItem() }

    /**
     * # TODO: Step 7
     *
     * Even though transaction management has been delegated to Spring, you may want to execute commands in batches for
     * performance.
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-execution/batch-execution/">Using JDBC batch operations</a>
     */
    override fun save(isbn: Isbn, title: String, authors: Collection<AuthorId>) {
        BookEntity(
            isbn = isbn.compressedValue,
            title = title,
            authors = authors.map { authorRepository.getReferenceById(it.value) }.toMutableList()
        ).let { bookRepository.save(it) }
    }

}
