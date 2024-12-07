package fr.sdecout.handson.persistence.book;

import fr.sdecout.handson.persistence.author.AuthorEntity;
import fr.sdecout.handson.persistence.library.LibraryEntity;
import fr.sdecout.handson.rest.book.BookResponse;
import fr.sdecout.handson.rest.book.BookSearchResponseItem;
import fr.sdecout.handson.rest.shared.BookField;
import fr.sdecout.handson.rest.shared.Isbn;
import jakarta.persistence.*;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static java.util.Collections.emptyList;

/** @deprecated Replace with jOOQ */
@Deprecated
@Entity
@Table(name = "BOOK")
public class BookEntity {

    @Id
    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "BOOK_AUTHOR",
            joinColumns = @JoinColumn(name = "BOOK", referencedColumnName = "ISBN"),
            inverseJoinColumns = @JoinColumn(name = "AUTHOR", referencedColumnName = "ID")
    )
    private List<AuthorEntity> authors;

    @ManyToMany(fetch = LAZY, mappedBy = "availableBooks")
    private List<LibraryEntity> availability = null;

    public static BookEntity from(String isbn, String title, List<AuthorEntity> authors) {
        var instance = new BookEntity();
        instance.isbn = isbn;
        instance.title = title;
        instance.authors = authors;
        return instance;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AuthorEntity> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorEntity> authors) {
        this.authors = authors;
    }

    public List<LibraryEntity> getAvailability() {
        return availability;
    }

    public void setAvailability(List<LibraryEntity> availability) {
        this.availability = availability;
    }

    public BookResponse toBookResponse() {
        return new BookResponse(
                new Isbn(isbn).formattedValue(),
                title,
                authors.stream().map(AuthorEntity::toAuthorField).toList(),
                availability == null ? emptyList() : availability.stream().map(LibraryEntity::toLibraryField).toList()
        );
    }

    public BookSearchResponseItem toBookSearchResponseItem() {
        return new BookSearchResponseItem(toBookField());
    }

    public BookField toBookField() {
        return new BookField(
                new Isbn(isbn).formattedValue(),
                title,
                authors.stream().map(AuthorEntity::toAuthorField).toList()
        );
    }

}
