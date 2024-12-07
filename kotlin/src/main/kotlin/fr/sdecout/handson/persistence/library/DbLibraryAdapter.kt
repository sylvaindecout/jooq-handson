package fr.sdecout.handson.persistence.library

import fr.sdecout.handson.persistence.book.BookRepository
import fr.sdecout.handson.persistence.jooq.tables.references.BOOK_AUTHOR
import fr.sdecout.handson.persistence.jooq.tables.references.LIBRARY
import fr.sdecout.handson.persistence.jooq.tables.references.LIBRARY_BOOK
import fr.sdecout.handson.rest.author.AuthorId
import fr.sdecout.handson.rest.library.*
import fr.sdecout.handson.rest.shared.AddressField
import fr.sdecout.handson.rest.shared.Isbn
import fr.sdecout.handson.rest.shared.LibraryField
import jakarta.transaction.Transactional
import org.jooq.DSLContext
import org.jooq.impl.DSL.countDistinct
import org.jooq.kotlin.fetchSingleValue
import org.springframework.stereotype.Component

@Component
@Transactional
class DbLibraryAdapter(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository,
    private val dsl: DSLContext,
) : LibraryAccess, LibrarySearch, LibraryCreation, BookCollectionUpdate {

    /**
     * # TODO: STEP 2
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
     */
    override fun findLibrary(id: LibraryId): LibraryResponse? = dsl
        .selectFrom(LIBRARY)
        .where(LIBRARY.ID.equal(id.value))
        .fetchAny { LibraryResponse(
            id = it.id,
            name = it.name,
            address = AddressField(it.addressLine_1, it.addressLine_2, it.postalCode, it.city),
        ) }

    /**
     * # TODO: STEP 2
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
     */
    override fun searchLibrariesClosestTo(postalCode: PostalCode): List<LibrarySearchResponseItem> = dsl
        .selectFrom(LIBRARY)
        .where(LIBRARY.POSTAL_CODE.startsWithIgnoreCase(postalCode.departmentCode))
        .fetch { LibrarySearchResponseItem(
            LibraryField(
                id = it.id,
                name = it.name,
                address = AddressField(it.addressLine_1, it.addressLine_2, it.postalCode, it.city),
            )
        ) }

    /**
     * # TODO: STEP 3
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-a-complex-table-expression">SELECT from a complex table expression</a>
     */
    override fun searchLibrariesWithBookAvailable(isbn: Isbn): List<LibrarySearchResponseItem> = dsl
        .select()
        .from(LIBRARY_BOOK).join(LIBRARY).on(LIBRARY.ID.equal(LIBRARY_BOOK.LIBRARY))
        .where(LIBRARY_BOOK.BOOK.equal(isbn))
        .fetch {
            LibrarySearchResponseItem(LibraryField(
                id = it.get(LIBRARY.ID),
                name = it.get(LIBRARY.NAME),
                address = AddressField(
                    it.get(LIBRARY.ADDRESS_LINE_1),
                    it.get(LIBRARY.ADDRESS_LINE_2),
                    it.get(LIBRARY.POSTAL_CODE),
                    it.get(LIBRARY.CITY)
                ),
            ))
        }

    /**
     * # TODO: STEP 6 // FIXME: Wont work with HAVING -> reconsider
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/having-clause/">HAVING clause</a>
     */
    override fun countLibrariesWithBooksBy(author: AuthorId): Long = dsl.select(countDistinct(LIBRARY_BOOK.LIBRARY))
        .from(LIBRARY_BOOK)
        .join(BOOK_AUTHOR).on(BOOK_AUTHOR.BOOK.equal(LIBRARY_BOOK.BOOK))
        .where(BOOK_AUTHOR.AUTHOR.equal(author.value))
        .fetchSingleValue().toLong()

    /**
     * # TODO: STEP 1
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/insert-statement/">The INSERT statement</a>
     */
    override fun addLibrary(name: String, address: AddressField): LibraryId = LibraryId.next().also { id ->
        dsl.insertInto(LIBRARY).set(LIBRARY.newRecord()
            .with(LIBRARY.ID, id.value)
            .with(LIBRARY.NAME, name)
            .with(LIBRARY.ADDRESS_LINE_1, address.line1)
            .with(LIBRARY.ADDRESS_LINE_2, address.line2)
            .with(LIBRARY.POSTAL_CODE, address.postalCode)
            .with(LIBRARY.CITY, address.city))
            .execute()
    }

    /**
     * # TODO: STEP 1
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/insert-statement/">The INSERT statement</a>
     */
    override fun addBook(libraryId: LibraryId, isbn: Isbn) {
        dsl.insertInto(LIBRARY_BOOK)
            .set(LIBRARY_BOOK.newRecord()
                .with(LIBRARY_BOOK.BOOK, isbn)
                .with(LIBRARY_BOOK.LIBRARY, libraryId.value))
            .onDuplicateKeyIgnore()
            .execute()
    }

}
