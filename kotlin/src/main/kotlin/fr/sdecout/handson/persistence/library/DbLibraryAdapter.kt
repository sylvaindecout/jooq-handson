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
     * # TODO: Step 1
     *
     * Let's start with some basic commands.
     *
     * The goal is to replace JPA with jOOQ.
     * You should run the tests before and after any change to check for regressions.
     *
     * *jOOQ has already been integrated (see `:heavy_plus_sign: Integrate jOOQ` commit).*
     * *In order to use it, you need to inject `DSLContext`.*
     *
     * *Do not forget to build, so that code modelling your DB schema is generated.*
     * *It could be implemented and maintained manually, but this is not something you want.*
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/insert-statement/">The INSERT statement</a>
     */
    override fun addLibrary(name: String, address: AddressField): LibraryId = LibraryId.next().also { id ->
        dsl.insertInto(LIBRARY)
            .set(LIBRARY.ID, id.value)
            .set(LIBRARY.NAME, name)
            .set(LIBRARY.ADDRESS_LINE_1, address.line1)
            .set(LIBRARY.ADDRESS_LINE_2, address.line2)
            .set(LIBRARY.POSTAL_CODE, address.postalCode)
            .set(LIBRARY.CITY, address.city)
            .execute()
    }

    /**
     * # TODO: Step 1
     *
     * @see addLibrary
     */
    override fun addBook(libraryId: LibraryId, isbn: Isbn) {
        dsl.insertInto(LIBRARY_BOOK)
            .set(LIBRARY_BOOK.BOOK, isbn.compressedValue)
            .set(LIBRARY_BOOK.LIBRARY, libraryId.value)
            .onDuplicateKeyIgnore()
            .execute()
    }

    /**
     * # TODO: Step 2
     *
     * Let's continue with some basic queries.
     *
     * Run the tests and check the console to see how jOOQ logs queries.
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-execution/fetching/">Fetching</a>
     */
    override fun findLibrary(id: LibraryId): LibraryResponse? =  dsl
        .selectFrom(LIBRARY)
        .where(LIBRARY.ID.equal(id.value))
        .fetchAny { LibraryResponse(
            id = it.id,
            name = it.name,
            address = AddressField(it.addressLine_1, it.addressLine_2, it.postalCode, it.city),
        ) }

    /**
     * # TODO: Step 2
     *
     * @see findLibrary
     */
    override fun searchLibrariesClosestTo(postalCode: PostalCode): List<LibrarySearchResponseItem> = dsl
        .selectFrom(LIBRARY)
        .where(LIBRARY.POSTAL_CODE.startsWithIgnoreCase(postalCode.departmentCode))
        .fetch { LibrarySearchResponseItem(
            id = it.id,
            name = it.name,
            address = AddressField(it.addressLine_1, it.addressLine_2, it.postalCode, it.city),
        ) }

    /**
     * # TODO: Step 3
     *
     * We have seen some basic queries.
     * But the main feature of relational databases is the relationships between tables, so let's perform some joins!
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-a-complex-table-expression">SELECT from a complex table expression</a>
     */
    override fun searchLibrariesWithBookAvailable(isbn: Isbn): List<LibrarySearchResponseItem> = dsl
        .select()
        .from(LIBRARY_BOOK).join(LIBRARY).on(LIBRARY.ID.equal(LIBRARY_BOOK.LIBRARY))
        .where(LIBRARY_BOOK.BOOK.equal(isbn.compressedValue))
        .fetch {
            LibrarySearchResponseItem(
                id = it.get(LIBRARY.ID),
                name = it.get(LIBRARY.NAME),
                address = AddressField(
                    it.get(LIBRARY.ADDRESS_LINE_1),
                    it.get(LIBRARY.ADDRESS_LINE_2),
                    it.get(LIBRARY.POSTAL_CODE),
                    it.get(LIBRARY.CITY)
                ),
            )
        }

    /**
     * # TODO: Step 3
     *
     * @see searchLibrariesWithBookAvailable
     */
    override fun countLibrariesWithBooksBy(author: AuthorId): Long =
        dsl.select(countDistinct(LIBRARY_BOOK.LIBRARY))
            .from(LIBRARY_BOOK)
            .join(BOOK_AUTHOR).on(BOOK_AUTHOR.BOOK.equal(LIBRARY_BOOK.BOOK))
            .where(BOOK_AUTHOR.AUTHOR.equal(author.value))
            .fetchSingleValue().toLong()

}
