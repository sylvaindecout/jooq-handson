package fr.sdecout.handson.persistence.book;

import fr.sdecout.handson.persistence.author.AuthorRepository;
import fr.sdecout.handson.rest.author.AuthorId;
import fr.sdecout.handson.rest.book.*;
import fr.sdecout.handson.rest.shared.Isbn;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Transactional
class DbBookAdapter implements BookAccess, BookSearch, BookUpdate {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    DbBookAdapter(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<BookResponse> findBook(Isbn isbn) {
        return bookRepository.findById(isbn.compressedValue())
                .map(BookEntity::toBookResponse);
    }

    @Override
    public Stream<BookSearchResponseItem> searchBooks(String hint) {
        return bookRepository.findByTitleLikeIgnoringCase('%' + hint + '%').stream()
                .map(BookEntity::toBookSearchResponseItem);
    }

    @Override
    public void save(Isbn isbn, String title, Collection<AuthorId> authors) {
        var entity = BookEntity.from(
                isbn.compressedValue(),
                title,
                authors.stream().map(AuthorId::value).map(authorRepository::getReferenceById).toList()
        );
        bookRepository.save(entity);
    }

}
