package fr.sdecout.handson.persistence.library

import fr.sdecout.handson.persistence.book.BookRepository
import fr.sdecout.handson.rest.author.AuthorId
import fr.sdecout.handson.rest.library.*
import fr.sdecout.handson.rest.shared.AddressField
import fr.sdecout.handson.rest.shared.Isbn
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@Transactional
class DbLibraryAdapter(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository,
) : LibraryAccess, LibrarySearch, LibraryCreation, BookCollectionUpdate {

    /**
     * # TODO: Step 1
     *
     * Let's start with some basic commands.
     *
     * *Do not forget to build, so that code modelling your DB schema is generated.*
     * *It could be implemented and maintained manually, but this is not something you want.*
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/insert-statement/">The INSERT statement</a>
     */
    override fun addLibrary(name: String, address: AddressField): LibraryId = LibraryId.next().also {
        libraryRepository.save(
            LibraryEntity(
                id = it.value,
                name = name,
                address = address.toAddressEntity(),
            )
        )
    }

    /**
     * # TODO: Step 1
     *
     * @see addLibrary
     */
    override fun addBook(libraryId: LibraryId, isbn: Isbn) {
        libraryRepository.getReferenceById(libraryId.value)
            .apply { bookRepository.getReferenceById(isbn.compressedValue).let { availableBooks?.add(it) } }
            .let { libraryRepository.save(it) }
    }

    /**
     * # TODO: Step 2
     *
     * Let's continue with some basic queries.
     *
     * Run the tests and check the console to see how jOOQ logs queries.
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
     */
    override fun findLibrary(id: LibraryId): LibraryResponse? = libraryRepository.findByIdOrNull(id.value)
        ?.toLibraryResponse()

    /**
     * # TODO: Step 2
     *
     * @see findLibrary
     */
    override fun searchLibrariesClosestTo(postalCode: PostalCode): List<LibrarySearchResponseItem> = libraryRepository
        .findByAddress_postalCodeStartingWith(postalCode.departmentCode)
        .map { it.toLibrarySearchResponseItem() }

    /**
     * # TODO: Step 3
     *
     * We have seen some basic queries.
     * But the main feature of relational databases is the relationships between tables, so let's perform some joins!
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-a-complex-table-expression">SELECT from a complex table expression</a>
     */
    override fun searchLibrariesWithBookAvailable(isbn: Isbn): List<LibrarySearchResponseItem> = libraryRepository
        .findByavailableBooks_isbn(isbn.compressedValue)
        .map { it.toLibrarySearchResponseItem() }

    /**
     * # TODO: Step 3
     *
     * @see searchLibrariesWithBookAvailable
     */
    override fun countLibrariesWithBooksBy(author: AuthorId): Long =
        libraryRepository.countLibrariesWithBooksBy(author.value)

}
