package fr.sdecout.handson.rest.book;

import fr.sdecout.handson.rest.author.AuthorId;
import fr.sdecout.handson.rest.shared.Isbn;

import java.util.Collection;

public interface BookUpdate {
    void save(Isbn isbn, String title, Collection<AuthorId> authors);
}
