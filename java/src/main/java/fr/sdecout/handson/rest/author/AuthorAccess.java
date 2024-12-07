package fr.sdecout.handson.rest.author;

import java.util.Optional;

public interface AuthorAccess {
    Optional<AuthorResponse> findAuthor(AuthorId id);
}
