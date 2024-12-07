package fr.sdecout.handson.persistence.library

import fr.sdecout.handson.persistence.book.BookRepository
import fr.sdecout.handson.rest.author.AuthorId
import fr.sdecout.handson.rest.book.Isbn
import fr.sdecout.handson.rest.library.*
import fr.sdecout.handson.rest.shared.AddressField
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@Transactional
class DbLibraryAdapter(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository,
) : LibraryAccess, LibrarySearch, LibraryCreation, BookCollectionUpdate {

    override fun findLibrary(id: LibraryId): LibraryResponse? = libraryRepository.findByIdOrNull(id.value)
        ?.toLibraryResponse()

    override fun searchLibrariesClosestTo(postalCode: PostalCode): List<LibrarySearchResponseItem> = libraryRepository
        .findByAddress_postalCodeStartingWith(postalCode.departmentCode)
        .map { it.toLibrarySearchResponseItem() }

    override fun searchLibrariesWithBookAvailable(isbn: Isbn): List<LibrarySearchResponseItem> = libraryRepository
        .findByavailableBooks_isbn(isbn.compressedValue)
        .map { it.toLibrarySearchResponseItem() }

    override fun countLibrariesWithBooksBy(author: AuthorId): Long = libraryRepository.countLibrariesWithBooksBy(author.value)

    override fun addLibrary(name: String, address: AddressField): LibraryId = LibraryId.next().also {
        libraryRepository.save(LibraryEntity(
            id = it.value,
            name = name,
            address = address.toAddressEntity(),
        ))
    }

    override fun addBook(libraryId: LibraryId, isbn: Isbn) {
        libraryRepository.getReferenceById(libraryId.value)
            .apply { bookRepository.getReferenceById(isbn.compressedValue).let { availableBooks?.add(it) } }
            .let { libraryRepository.save(it) }
    }

}
