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

    /**
     * <h1>TODO: Step 4</h1>
     * <p>At this stage, we can deal with 95% of our use cases. Now let's get a step further.</p>
     * <p>
     * 1-N relationships can be dealt with multiple queries, but standard SQL includes the capability to nest queries
     * with <code>MULTISET</code> function.
     * </p>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-building/column-expressions/multiset-value-constructor/">MULTISET value constructor</a>
     */
    @Override
    public Stream<BookSearchResponseItem> searchBooks(String hint) {
        return bookRepository.findByTitleLikeIgnoringCase('%' + hint + '%').stream()
                .map(BookEntity::toBookSearchResponseItem);
    }

    /**
     * <h1>TODO: Step 6</h1>
     * <p>
     * Even though transaction management has been delegated to Spring, you may want to execute commands in batches for
     * performance.
     * </p>
     *
     * @see <a href="https://www.jooq.org/doc/latest/manual/sql-execution/batch-execution/">Using JDBC batch operations</a>
     */
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
