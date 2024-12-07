package fr.sdecout.handson.persistence.library;

import fr.sdecout.handson.persistence.book.BookEntity;
import fr.sdecout.handson.rest.library.LibraryResponse;
import fr.sdecout.handson.rest.library.LibrarySearchResponseItem;
import fr.sdecout.handson.rest.shared.AddressField;
import fr.sdecout.handson.rest.shared.LibraryField;
import jakarta.persistence.*;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static java.util.Collections.emptyList;

@Entity
@Table(name = "LIBRARY")
public class LibraryEntity {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private AddressEntity address;

    @ManyToMany(fetch = LAZY)
    @JoinTable(
            name = "LIBRARY_BOOK",
            joinColumns = @JoinColumn(name = "LIBRARY", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "BOOK", referencedColumnName = "ISBN")
    )
    private List<BookEntity> availableBooks = null;

    public static LibraryEntity from(String id, String name, AddressField address) {
        var instance = new LibraryEntity();
        instance.id = id;
        instance.name = name;
        instance.address = AddressEntity.from(address);
        instance.availableBooks = emptyList();
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public List<BookEntity> getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(List<BookEntity> availableBooks) {
        this.availableBooks = availableBooks;
    }

    @Override
    public String toString() {
        return "LibraryEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", availableBooks=" + availableBooks +
                '}';
    }

    public LibraryResponse toLibraryResponse() {
        return new LibraryResponse(
                this.id,
                this.name,
                this.address.toAddressField()
        );
    }

    public LibrarySearchResponseItem toLibrarySearchResponseItem() {
        return new LibrarySearchResponseItem(
                this.id,
                this.name,
                this.address.toAddressField()
        );
    }

    public LibraryField toLibraryField() {
        return new LibraryField(
                this.id,
                this.name,
                this.address.toAddressField()
        );
    }

}
