package fr.sdecout.handson.persistence.author;

import fr.sdecout.handson.persistence.book.BookEntity;
import fr.sdecout.handson.rest.author.AuthorResponse;
import fr.sdecout.handson.rest.author.AuthorSearchResponseItem;
import fr.sdecout.handson.rest.shared.AuthorField;
import jakarta.persistence.*;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static java.util.Collections.emptyList;

@Entity
@Table(name = "AUTHOR")
public class AuthorEntity {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @ManyToMany(fetch = LAZY, cascade = ALL, mappedBy = "authors")
    private List<BookEntity> publishedBooks = null;

    public static AuthorEntity from(String id, String lastName, String firstName) {
        var instance = new AuthorEntity();
        instance.id = id;
        instance.lastName = lastName;
        instance.firstName = firstName;
        instance.publishedBooks = emptyList();
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<BookEntity> getPublishedBooks() {
        return publishedBooks;
    }

    public void setPublishedBooks(List<BookEntity> publishedBooks) {
        this.publishedBooks = publishedBooks;
    }

    public AuthorResponse toAuthorResponse() {
        return new AuthorResponse(
                this.id,
                this.lastName,
                this.firstName,
                this.publishedBooks == null ? emptyList() : this.publishedBooks.stream().map(BookEntity::toBookField).toList()
        );
    }

    public AuthorSearchResponseItem toAuthorSearchResponseItem() {
        return new AuthorSearchResponseItem(
                this.id,
                this.lastName,
                this.firstName
        );
    }

    public AuthorField toAuthorField() {
        return new AuthorField(
                this.id,
                this.lastName,
                this.firstName
        );
    }

}
