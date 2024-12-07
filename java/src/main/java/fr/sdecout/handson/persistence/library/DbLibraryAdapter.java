package fr.sdecout.handson.persistence.library;

import fr.sdecout.handson.persistence.book.BookRepository;
import fr.sdecout.handson.rest.author.AuthorId;
import fr.sdecout.handson.rest.library.*;
import fr.sdecout.handson.rest.shared.AddressField;
import fr.sdecout.handson.rest.shared.Isbn;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@Transactional
class DbLibraryAdapter implements LibraryAccess, LibrarySearch, LibraryCreation, BookCollectionUpdate {

    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    DbLibraryAdapter(LibraryRepository libraryRepository, BookRepository bookRepository) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public LibraryId addLibrary(String name, AddressField address) {
        var libraryId = LibraryId.next();
        libraryRepository.save(LibraryEntity.from(
                libraryId.value(),
                name,
                address
        ));
        return libraryId;
    }

    @Override
    public void addBook(LibraryId libraryId, Isbn isbn) {
        var library = libraryRepository.getReferenceById(libraryId.value());
        var book = bookRepository.getReferenceById(isbn.compressedValue());
        library.getAvailableBooks().add(book);
        libraryRepository.save(library);
    }

    @Override
    public Optional<LibraryResponse> findLibrary(LibraryId id) {
        return libraryRepository.findById(id.value())
                .map(LibraryEntity::toLibraryResponse);
    }

    @Override
    public Stream<LibrarySearchResponseItem> searchLibrariesClosestTo(PostalCode postalCode) {
        return libraryRepository.findByAddress_postalCodeStartingWith(postalCode.departmentCode()).stream()
                .map(LibraryEntity::toLibrarySearchResponseItem);
    }

    @Override
    public Stream<LibrarySearchResponseItem> searchLibrariesWithBookAvailable(Isbn isbn) {
        return libraryRepository.findByavailableBooks_isbn(isbn.compressedValue()).stream()
                .map(LibraryEntity::toLibrarySearchResponseItem);
    }

    @Override
    public Long countLibrariesWithBooksBy(AuthorId author) {
        return libraryRepository.countLibrariesWithBooksBy(author.value());
    }

}
