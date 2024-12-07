package fr.sdecout.handson.persistence.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<BookEntity, String> {
    Stream<BookEntity> findByTitleLikeIgnoringCase(String hint);
}
