package fr.sdecout.handson.persistence.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, String> {
    List<BookEntity> findByTitleLikeIgnoringCase(String hint);
}
