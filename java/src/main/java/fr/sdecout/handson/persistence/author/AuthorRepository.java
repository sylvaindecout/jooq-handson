package fr.sdecout.handson.persistence.author;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {
    Stream<AuthorEntity> findByLastNameLikeIgnoringCase(String hint);
}
