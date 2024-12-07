package fr.sdecout.handson.rest.book;

import java.util.Optional;

public interface BookAccess {
    Optional<BookResponse> findBook(Isbn isbn);
}
