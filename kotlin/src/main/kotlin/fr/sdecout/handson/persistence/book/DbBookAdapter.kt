package fr.sdecout.handson.persistence.book

import fr.sdecout.handson.persistence.author.AuthorRepository
import fr.sdecout.handson.persistence.jooq.tables.records.BookAuthorRecord
import fr.sdecout.handson.persistence.jooq.tables.records.BookRecord
import fr.sdecout.handson.persistence.jooq.tables.references.AUTHOR
import fr.sdecout.handson.persistence.jooq.tables.references.BOOK
import fr.sdecout.handson.persistence.jooq.tables.references.BOOK_AUTHOR
import fr.sdecout.handson.rest.author.AuthorId
import fr.sdecout.handson.rest.book.*
import fr.sdecout.handson.rest.shared.AuthorField
import fr.sdecout.handson.rest.shared.Isbn
import jakarta.transaction.Transactional
import org.jooq.DSLContext
import org.jooq.DeleteConditionStep
import org.jooq.InsertOnDuplicateSetMoreStep
import org.jooq.InsertReturningStep
import org.jooq.impl.DSL.multiset
import org.jooq.impl.DSL.select
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@Transactional
class DbBookAdapter(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val dsl: DSLContext,
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
     * @see <a href="https://blog.jooq.org/jooq-3-15s-new-multiset-operator-will-change-how-you-think-about-sql/">jOOQ 3.15’s New Multiset Operator Will Change How You Think About SQL</a>
     */
    override fun searchBooks(hint: String): List<BookSearchResponseItem> = dsl
        .select(
            BOOK.ISBN,
            BOOK.TITLE,
            bookAuthors,
        )
        .from(BOOK)
        .where(BOOK.TITLE.likeIgnoreCase("%$hint%"))
        .fetch { BookSearchResponseItem(
            isbn = it.get(BOOK.ISBN)?.formattedValue,
            title = it.get(BOOK.TITLE),
            authors = it.get(bookAuthors),
        ) }

    /**
     * # TODO: Step 5
     *
     * Even though transaction management has been delegated to Spring, you may want to execute commands in batches for
     * performance.
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-execution/batch-execution/">Using JDBC batch operations</a>
     */
    override fun save(isbn: Isbn, title: String, authors: Collection<AuthorId>) {
        val upsertOperation = prepareBookUpdate(isbn, title)
        val removeAuthorOperation = prepareAuthorRemovals(isbn, authors)
        val addAuthorOperations = prepareAuthorInsertions(isbn, authors)
        val queries = listOf(upsertOperation) + removeAuthorOperation + addAuthorOperations
        dsl.batch(queries).execute()
    }

    private fun prepareBookUpdate(isbn: Isbn, title: String): InsertOnDuplicateSetMoreStep<BookRecord> = BOOK.newRecord()
        .with(BOOK.ISBN, isbn)
        .with(BOOK.TITLE, title)
        .let { dsl.insertInto(BOOK).set(it).onDuplicateKeyUpdate().set(it) }

    private fun prepareAuthorRemovals(isbn: Isbn, authors: Collection<AuthorId>): DeleteConditionStep<BookAuthorRecord> = dsl
        .deleteFrom(BOOK_AUTHOR)
        .where(BOOK_AUTHOR.BOOK.equal(isbn))
        .and(BOOK_AUTHOR.AUTHOR.notIn(authors))

    private fun prepareAuthorInsertions(isbn: Isbn, authors: Collection<AuthorId>): List<InsertReturningStep<BookAuthorRecord>> = authors
        .map { authorId -> BOOK_AUTHOR.newRecord()
            .with(BOOK_AUTHOR.BOOK, isbn)
            .with(BOOK_AUTHOR.AUTHOR, authorId.value) }
        .map { dsl.insertInto(BOOK_AUTHOR).set(it).onDuplicateKeyIgnore() }

}

val bookAuthors = multiset(
    select(AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
        .from(AUTHOR.join(BOOK_AUTHOR).on(BOOK_AUTHOR.AUTHOR.eq(AUTHOR.ID)))
        .where(BOOK_AUTHOR.BOOK.eq(BOOK.ISBN))
).`as`("authors").convertFrom { record -> record.map { AuthorField(
    id = it.get(AUTHOR.ID),
    lastName = it.get(AUTHOR.LAST_NAME),
    firstName = it.get(AUTHOR.FIRST_NAME),
) } }
