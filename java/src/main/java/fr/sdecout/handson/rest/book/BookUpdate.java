package fr.sdecout.handson.rest.book;

import fr.sdecout.handson.rest.author.AuthorId;

import java.util.Collection;
import java.util.stream.Stream;

public interface BookUpdate {
    void save(Isbn isbn, String title, Collection<AuthorId> authors);
}
