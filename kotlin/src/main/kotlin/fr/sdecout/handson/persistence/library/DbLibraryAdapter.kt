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
     * # TODO: STEP 2
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
     */
    override fun findLibrary(id: LibraryId): LibraryResponse? = libraryRepository.findByIdOrNull(id.value)
        ?.toLibraryResponse()

    /**
     * # TODO: STEP 2
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
     */
    override fun searchLibrariesClosestTo(postalCode: PostalCode): List<LibrarySearchResponseItem> = libraryRepository
        .findByAddress_postalCodeStartingWith(postalCode.departmentCode)
        .map { it.toLibrarySearchResponseItem() }

    /**
     * # TODO: STEP 3
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-a-complex-table-expression">SELECT from a complex table expression</a>
     */
    override fun searchLibrariesWithBookAvailable(isbn: Isbn): List<LibrarySearchResponseItem> = libraryRepository
        .findByavailableBooks_isbn(isbn.compressedValue)
        .map { it.toLibrarySearchResponseItem() }

    /**
     * # TODO: STEP 3
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-a-complex-table-expression">SELECT from a complex table expression</a>
     */
    override fun countLibrariesWithBooksBy(author: AuthorId): Long =
        libraryRepository.countLibrariesWithBooksBy(author.value)

    /**
     * # TODO: STEP 1
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
     * # TODO: STEP 1
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/insert-statement/">The INSERT statement</a>
     */
    override fun addBook(libraryId: LibraryId, isbn: Isbn) {
        libraryRepository.getReferenceById(libraryId.value)
            .apply { bookRepository.getReferenceById(isbn.compressedValue).let { availableBooks?.add(it) } }
            .let { libraryRepository.save(it) }
    }

}
