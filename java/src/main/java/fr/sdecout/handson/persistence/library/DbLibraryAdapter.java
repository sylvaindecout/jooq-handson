package fr.sdecout.handson.persistence.library;

import fr.sdecout.handson.persistence.book.BookRepository;
import fr.sdecout.handson.rest.author.AuthorId;
import fr.sdecout.handson.rest.library.*;
import fr.sdecout.handson.rest.shared.AddressField;
import fr.sdecout.handson.rest.shared.Isbn;
import jakarta.transaction.Transactional;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

import static fr.sdecout.handson.persistence.jooq.Tables.*;
import static org.jooq.impl.DSL.count;

@Component
@Transactional
class DbLibraryAdapter implements LibraryAccess, LibrarySearch, LibraryCreation, BookCollectionUpdate {

    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;
    private final DSLContext dsl;

    DbLibraryAdapter(LibraryRepository libraryRepository, BookRepository bookRepository, DSLContext dsl) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
        this.dsl = dsl;
    }

    /**
     * <h1>TODO: Step 1</h1>
     * <p>Let's start with some basic commands.</p>
     * <p>
     * The goal is to replace JPA with jOOQ.
     * You should run the tests before and after any change to check for regressions.
     * </p>
     * <p><i>
     * jOOQ has already been integrated (see <code>:heavy_plus_sign: Integrate jOOQ</code> commit).
     * In order to use it, you need to inject <code>DSLContext</code>.
     * </i></p>
     * <p><i>
     * Do not forget to build, so that code modelling your DB schema is generated.
     * It could be implemented and maintained manually, but this is not something you want.
     * </i></p>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/insert-statement/">The INSERT statement</a>
     */
    @Override
    public LibraryId addLibrary(String name, AddressField address) {
        var libraryId = LibraryId.next();
        dsl.insertInto(LIBRARY)
                .set(LIBRARY.ID, libraryId.value())
                .set(LIBRARY.NAME, name)
                .set(LIBRARY.ADDRESS_LINE_1, address.line1())
                .set(LIBRARY.ADDRESS_LINE_2, address.line2())
                .set(LIBRARY.POSTAL_CODE, address.postalCode())
                .set(LIBRARY.CITY, address.city())
                .execute();
        return libraryId;
    }

    /**
     * <h1>TODO: Step 1</h1>
     *
     * @see #addLibrary
     */
    @Override
    public void addBook(LibraryId libraryId, Isbn isbn) {
        dsl.insertInto(LIBRARY_BOOK)
                .set(LIBRARY_BOOK.BOOK, isbn.compressedValue())
                .set(LIBRARY_BOOK.LIBRARY, libraryId.value())
                .onDuplicateKeyIgnore()
                .execute();
    }

    /**
     * <h1>TODO: Step 2</h1>
     * <p>Let's continue with some basic queries.</p>
     * <p>Run the tests and check the console to see how jOOQ logs queries.</p>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-execution/fetching/">Fetching</a>
     */
    @Override
    public Optional<LibraryResponse> findLibrary(LibraryId id) {
        return dsl.selectFrom(LIBRARY)
                .where(LIBRARY.ID.equal(id.value()))
                .fetchOptional(it -> new LibraryResponse(
                        it.getId(),
                        it.getName(),
                        new AddressField(it.getAddressLine_1(), it.getAddressLine_2(), it.getPostalCode(), it.getCity()))
                );
    }

    /**
     * <h1>TODO: Step 2</h1>
     *
     * @see #findLibrary
     */
    @Override
    public Stream<LibrarySearchResponseItem> searchLibrariesClosestTo(PostalCode postalCode) {
        return dsl.selectFrom(LIBRARY)
                .where(LIBRARY.POSTAL_CODE.startsWithIgnoreCase(postalCode.departmentCode()))
                .fetch(it -> new LibrarySearchResponseItem(
                        it.getId(),
                        it.getName(),
                        new AddressField(it.getAddressLine_1(), it.getAddressLine_2(), it.getPostalCode(), it.getCity()))
                ).stream();
    }

    /**
     * <h1>TODO: Step 3</h1>
     * <p>
     * We have seen some basic queries.
     * But the main feature of relational databases is the relationships between tables, so let's perform some joins!
     * </p>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-a-complex-table-expression">SELECT from a complex table expression</a>
     */
    @Override
    public Stream<LibrarySearchResponseItem> searchLibrariesWithBookAvailable(Isbn isbn) {
        return dsl.select()
                .from(LIBRARY_BOOK).join(LIBRARY).on(LIBRARY.ID.equal(LIBRARY_BOOK.LIBRARY))
                .where(LIBRARY_BOOK.BOOK.equal(isbn.compressedValue()))
                .fetch(it -> new LibrarySearchResponseItem(
                        it.get(LIBRARY.ID),
                        it.get(LIBRARY.NAME),
                        new AddressField(
                                it.get(LIBRARY.ADDRESS_LINE_1),
                                it.get(LIBRARY.ADDRESS_LINE_2),
                                it.get(LIBRARY.POSTAL_CODE),
                                it.get(LIBRARY.CITY)
                        )
                )).stream();
    }

    /**
     * <h1>TODO: Step 3</h1>
     *
     * @see #searchLibrariesWithBookAvailable
     */
    @Override
    public Long countLibrariesWithBooksBy(AuthorId author) {
        return dsl.select(count())
                .from(LIBRARY_BOOK)
                .join(BOOK_AUTHOR).on(BOOK_AUTHOR.BOOK.equal(LIBRARY_BOOK.BOOK))
                .where(BOOK_AUTHOR.AUTHOR.equal(author.value()))
                .fetchSingle(Record1::value1).longValue();

    }

}
