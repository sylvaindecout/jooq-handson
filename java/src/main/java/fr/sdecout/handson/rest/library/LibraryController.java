package fr.sdecout.handson.rest.library;

import fr.sdecout.handson.rest.author.AuthorId;
import fr.sdecout.handson.rest.book.Isbn;
import fr.sdecout.handson.rest.shared.ValidIsbn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/libraries")
public class LibraryController {

    private final LibraryAccess libraryAccess;
    private final LibrarySearch librarySearch;
    private final LibraryCreation libraryCreation;
    private final BookCollectionUpdate bookCollectionUpdate;

    LibraryController(
            LibraryAccess libraryAccess,
            LibrarySearch librarySearch,
            LibraryCreation libraryCreation,
            BookCollectionUpdate bookCollectionUpdate
    ) {
        this.libraryAccess = libraryAccess;
        this.librarySearch = librarySearch;
        this.libraryCreation = libraryCreation;
        this.bookCollectionUpdate = bookCollectionUpdate;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LibraryResponse> getLibrary(@PathVariable String id) {
        return libraryAccess.findLibrary(new LibraryId(id))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public Stream<LibrarySearchResponseItem> searchLibraries(
            @RequestParam(required = false) String postalCode,
            @ValidIsbn @RequestParam(required = false) String isbn
    ) {
        if (postalCode != null) {
            return librarySearch.searchLibrariesClosestTo(new PostalCode(postalCode));
        }
        if (isbn != null) {
            return librarySearch.searchLibrariesWithBookAvailable(new Isbn(isbn));
        }
        throw new ResponseStatusException(BAD_REQUEST, "At least one criterion must be provided");
    }

    @PostMapping(value = "/count", produces = APPLICATION_JSON_VALUE)
    public Long countLibraries(@NotBlank @RequestParam String author) {
        return librarySearch.countLibrariesWithBooksBy(new AuthorId(author));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addLibrary(@Valid @RequestBody LibraryCreationRequest requestBody) {
        var libraryId = libraryCreation.addLibrary(requestBody.name(), requestBody.address());
        var location = URI.create("/libraries/" + libraryId.value());
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{libraryId}/books")
    @ResponseStatus(NO_CONTENT)
    public void addBook(@PathVariable String libraryId, @NotNull @ValidIsbn @RequestParam String isbn) {
        bookCollectionUpdate.addBook(new LibraryId(libraryId), new Isbn(isbn));
    }

}
