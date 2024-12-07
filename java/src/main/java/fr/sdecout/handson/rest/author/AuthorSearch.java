package fr.sdecout.handson.rest.author;

import java.util.stream.Stream;

public interface AuthorSearch {
    Stream<AuthorSearchResponseItem> searchAuthors(String hint);
}
