package fr.sdecout.handson.persistence.library;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface LibraryRepository extends JpaRepository<LibraryEntity, String> {

    Stream<LibraryEntity> findByAddress_postalCodeStartingWith(String departmentId);

    Stream<LibraryEntity> findByavailableBooks_isbn(String isbn);

    @Query("SELECT COUNT(DISTINCT l.id) FROM LibraryEntity l JOIN l.availableBooks b JOIN b.authors a WHERE a.id = :authorId")
    Long countLibrariesWithBooksBy(String authorId);

}
