package fr.sdecout.handson.persistence.author;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {
    List<AuthorEntity> findByLastNameLikeIgnoringCase(String hint);
}
