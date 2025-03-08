package fr.sdecout.handson.persistence.book;

import fr.sdecout.handson.persistence.author.AuthorRepository;
import fr.sdecout.handson.persistence.jooq.tables.records.BookAuthorRecord;
import fr.sdecout.handson.persistence.jooq.tables.records.BookRecord;
import fr.sdecout.handson.rest.author.AuthorId;
import fr.sdecout.handson.rest.book.*;
import fr.sdecout.handson.rest.shared.AuthorField;
import fr.sdecout.handson.rest.shared.Isbn;
import jakarta.transaction.Transactional;
import org.jooq.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static fr.sdecout.handson.persistence.jooq.Tables.*;
import static java.util.stream.Stream.concat;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

@Component
@Transactional
class DbBookAdapter implements BookAccess, BookSearch, BookUpdate {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final DSLContext dsl;

    DbBookAdapter(BookRepository bookRepository, AuthorRepository authorRepository, DSLContext dsl) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.dsl = dsl;
    }

    @Override
    public Optional<BookResponse> findBook(Isbn isbn) {
        return bookRepository.findById(isbn.compressedValue())
                .map(BookEntity::toBookResponse);
    }

    /**
     * <h1>TODO: Step 4</h1>
     * <p>At this stage, we can deal with 95% of our use cases. Now let's get a step further.</p>
     * <p>
     * 1-N relationships can be dealt with multiple queries, but standard SQL includes the capability to nest queries
     * with <code>MULTISET</code> function.
     * </p>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/multiset-value-constructor/">MULTISET value constructor</a>
     */
    @Override
    public Stream<BookSearchResponseItem> searchBooks(String hint) {
        Field<List<AuthorField>> bookAuthors = multiset(
                select(AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                        .from(AUTHOR.join(BOOK_AUTHOR).on(BOOK_AUTHOR.AUTHOR.eq(AUTHOR.ID)))
                        .where(BOOK_AUTHOR.BOOK.eq(BOOK.ISBN))
        ).as("authors").convertFrom(record -> record.map(it -> new AuthorField(
                it.get(AUTHOR.ID),
                it.get(AUTHOR.LAST_NAME),
                it.get(AUTHOR.FIRST_NAME)
        )));
        return dsl.select(BOOK.ISBN, BOOK.TITLE, bookAuthors)
                .from(BOOK)
                .where(BOOK.TITLE.likeIgnoreCase("%" + hint + "%"))
                .fetch(it -> new BookSearchResponseItem(
                        it.get(BOOK.ISBN).formattedValue(),
                        it.get(BOOK.TITLE),
                        it.get(bookAuthors)
                )).stream();
    }

    /**
     * <h1>TODO: Step 5</h1>
     * <p>
     * Even though transaction management has been delegated to Spring, you may want to execute commands in batches for
     * performance.
     * </p>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-execution/batch-execution/">Using JDBC batch operations</a>
     */
    @Override
    public void save(Isbn isbn, String title, Collection<AuthorId> authors) {
        var upsertOperation = prepareBookUpdate(isbn, title);
        var removeAuthorOperation = prepareAuthorRemovals(isbn, authors);
        var addAuthorOperations = prepareAuthorInsertions(isbn, authors);
        var queries = concat(Stream.of(upsertOperation, removeAuthorOperation), addAuthorOperations).toList();
        dsl.batch(queries).execute();
    }

    private InsertOnDuplicateSetMoreStep<BookRecord> prepareBookUpdate(Isbn isbn, String title) {
        var row = BOOK.newRecord()
                .with(BOOK.ISBN, isbn)
                .with(BOOK.TITLE, title);
        return dsl.insertInto(BOOK).set(row).onDuplicateKeyUpdate().set(row);
    }

    private DeleteConditionStep<BookAuthorRecord> prepareAuthorRemovals(Isbn isbn, Collection<AuthorId> authors) {
        return dsl.deleteFrom(BOOK_AUTHOR)
                .where(BOOK_AUTHOR.BOOK.equal(isbn))
                .and(BOOK_AUTHOR.AUTHOR.notIn(authors));
    }

    private Stream<InsertReturningStep<BookAuthorRecord>> prepareAuthorInsertions(Isbn isbn, Collection<AuthorId> authors) {
        return authors.stream()
                .map(authorId -> BOOK_AUTHOR.newRecord()
                        .with(BOOK_AUTHOR.BOOK, isbn)
                        .with(BOOK_AUTHOR.AUTHOR, authorId.value()))
                .map(it -> dsl.insertInto(BOOK_AUTHOR).set(it).onDuplicateKeyIgnore());
    }

}
