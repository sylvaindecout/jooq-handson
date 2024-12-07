package fr.sdecout.handson.rest.library;

import fr.sdecout.handson.rest.author.AuthorId;
import fr.sdecout.handson.rest.book.Isbn;

import java.util.stream.Stream;

public interface LibrarySearch {
    Stream<LibrarySearchResponseItem> searchLibrariesClosestTo(PostalCode postalCode);
    Stream<LibrarySearchResponseItem> searchLibrariesWithBookAvailable(Isbn isbn);
    Long countLibrariesWithBooksBy(AuthorId author);
}
