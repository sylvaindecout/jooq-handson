package fr.sdecout.handson.persistence.author;

import fr.sdecout.handson.rest.author.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@Transactional
class DbAuthorAdapter implements AuthorAccess, AuthorCreation, AuthorSearch {

    private final AuthorRepository authorRepository;

    DbAuthorAdapter(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<AuthorResponse> findAuthor(AuthorId id) {
        return authorRepository.findById(id.value())
                .map(AuthorEntity::toAuthorResponse);
    }

    @Override
    public Stream<AuthorSearchResponseItem> searchAuthors(String hint) {
        return authorRepository.findByLastNameLikeIgnoringCase('%' + hint + '%')
                .map( AuthorEntity::toAuthorSearchResponseItem);
    }

    @Override
    public AuthorId addAuthor(String lastName, String firstName) {
        var authorId = AuthorId.next();
        authorRepository.save(AuthorEntity.from(authorId.value(), lastName, firstName));
        return authorId;
    }

}
