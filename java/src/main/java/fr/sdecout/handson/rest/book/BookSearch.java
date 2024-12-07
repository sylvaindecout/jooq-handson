package fr.sdecout.handson.rest.book;

import java.util.stream.Stream;

public interface BookSearch {
    Stream<BookSearchResponseItem> searchBooks(String hint);
}
