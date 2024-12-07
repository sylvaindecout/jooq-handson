package fr.sdecout.handson.persistence.library;

import fr.sdecout.handson.persistence.book.BookRepository;
import fr.sdecout.handson.rest.author.AuthorId;
import fr.sdecout.handson.rest.library.*;
import fr.sdecout.handson.rest.shared.AddressField;
import fr.sdecout.handson.rest.shared.Isbn;
import fr.sdecout.handson.rest.shared.LibraryField;
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
     * <h1>TODO: STEP 2</h1>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
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
     * <h1>TODO: STEP 2</h1>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
     */
    @Override
    public Stream<LibrarySearchResponseItem> searchLibrariesClosestTo(PostalCode postalCode) {
        return dsl.selectFrom(LIBRARY)
                .where(LIBRARY.POSTAL_CODE.startsWithIgnoreCase(postalCode.departmentCode()))
                .fetch(it -> new LibrarySearchResponseItem(
                    new LibraryField(
                        it.getId(),
                        it.getName(),
                        new AddressField(it.getAddressLine_1(), it.getAddressLine_2(), it.getPostalCode(), it.getCity()))
                )).stream();
    }

    /**
     * <h1>TODO: STEP 3</h1>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-a-complex-table-expression">SELECT from a complex table expression</a>
     */
    @Override
    public Stream<LibrarySearchResponseItem> searchLibrariesWithBookAvailable(Isbn isbn) {
        return dsl.select()
                .from(LIBRARY_BOOK).join(LIBRARY).on(LIBRARY.ID.equal(LIBRARY_BOOK.LIBRARY))
                .where(LIBRARY_BOOK.BOOK.equal(isbn))
                .fetch(it -> new LibrarySearchResponseItem(new LibraryField(
                    it.get(LIBRARY.ID),
                    it.get(LIBRARY.NAME),
                    new AddressField(
                            it.get(LIBRARY.ADDRESS_LINE_1),
                            it.get(LIBRARY.ADDRESS_LINE_2),
                            it.get(LIBRARY.POSTAL_CODE),
                            it.get(LIBRARY.CITY)
                    )
                ))).stream();
    }

    /**
     * <h1>TODO: STEP 6</h1> // FIXME: Wont work with HAVING -> reconsider
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/having-clause/">HAVING clause</a>
     */
    @Override
    public Long countLibrariesWithBooksBy(AuthorId author) {
        return dsl.select(count())
                .from(LIBRARY_BOOK)
                .join(BOOK_AUTHOR).on(BOOK_AUTHOR.BOOK.equal(LIBRARY_BOOK.BOOK))
                .where(BOOK_AUTHOR.AUTHOR.equal(author.value()))
                .fetchSingle(Record1::value1).longValue();
    }

    /**
     * <h1>TODO: STEP 1</h1>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/insert-statement/">The INSERT statement</a>
     */
    @Override
    public LibraryId addLibrary(String name, AddressField address) {
        var libraryId = LibraryId.next();
        dsl.insertInto(LIBRARY).set(LIBRARY.newRecord()
                        .with(LIBRARY.ID, libraryId.value())
                        .with(LIBRARY.NAME, name)
                        .with(LIBRARY.ADDRESS_LINE_1, address.line1())
                        .with(LIBRARY.ADDRESS_LINE_2, address.line2())
                        .with(LIBRARY.POSTAL_CODE, address.postalCode())
                        .with(LIBRARY.CITY, address.city()))
                .execute();
        return libraryId;
    }

    /**
     * <h1>TODO: STEP 1</h1>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/insert-statement/">The INSERT statement</a>
     */
    @Override
    public void addBook(LibraryId libraryId, Isbn isbn) {
        dsl.insertInto(LIBRARY_BOOK)
                .set(LIBRARY_BOOK.newRecord()
                        .with(LIBRARY_BOOK.BOOK, isbn)
                        .with(LIBRARY_BOOK.LIBRARY, libraryId.value()))
                .onDuplicateKeyIgnore()
                .execute();
    }

}
