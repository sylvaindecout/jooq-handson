package fr.sdecout.handson.rest.library;

import fr.sdecout.handson.rest.book.Isbn;

public interface BookCollectionUpdate {
    void addBook(LibraryId libraryId, Isbn isbn);
}
