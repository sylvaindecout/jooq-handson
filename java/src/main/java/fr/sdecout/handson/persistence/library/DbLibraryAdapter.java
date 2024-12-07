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

    /**
     * <h1>TODO: Step 2</h1>
     * <p>Let's continue with some basic queries.</p>
     * <p>Run the tests and check the console to see how jOOQ logs queries.</p>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/#select-from-single-tables">SELECT from single tables</a>
     */
    @Override
    public Optional<LibraryResponse> findLibrary(LibraryId id) {
        return libraryRepository.findById(id.value())
                .map(LibraryEntity::toLibraryResponse);
    }

    /**
     * <h1>TODO: Step 2</h1>
     *
     * @see #findLibrary
     */
    @Override
    public Stream<LibrarySearchResponseItem> searchLibrariesClosestTo(PostalCode postalCode) {
        return libraryRepository.findByAddress_postalCodeStartingWith(postalCode.departmentCode()).stream()
                .map(LibraryEntity::toLibrarySearchResponseItem);
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
        return libraryRepository.findByavailableBooks_isbn(isbn.compressedValue()).stream()
                .map(LibraryEntity::toLibrarySearchResponseItem);
    }

    /**
     * <h1>TODO: Step 3</h1>
     *
     * @see #searchLibrariesWithBookAvailable
     */
    @Override
    public Long countLibrariesWithBooksBy(AuthorId author) {
        return libraryRepository.countLibrariesWithBooksBy(author.value());
    }

    /**
     * <h1>TODO: Step 1</h1>
     * <p>Let's start with some basic commands.</p>
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
        libraryRepository.save(LibraryEntity.from(
                libraryId.value(),
                name,
                address
        ));
        return libraryId;
    }

    /**
     * <h1>TODO: Step 1</h1>
     *
     * @see #addLibrary
     */
    @Override
    public void addBook(LibraryId libraryId, Isbn isbn) {
        var library = libraryRepository.getReferenceById(libraryId.value());
        var book = bookRepository.getReferenceById(isbn.compressedValue());
        library.getAvailableBooks().add(book);
        libraryRepository.save(library);
    }

}
