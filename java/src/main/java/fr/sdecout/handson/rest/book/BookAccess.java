package fr.sdecout.handson.rest.book;

import fr.sdecout.handson.rest.shared.Isbn;

import java.util.Optional;

public interface BookAccess {
    Optional<BookResponse> findBook(Isbn isbn);
}
