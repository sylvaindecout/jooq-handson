package fr.sdecout.handson.persistence.library;

import fr.sdecout.handson.TestApp;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static fr.sdecout.handson.rest.TestData.BNF;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Tag("Integration")
@SpringBootTest(classes = TestApp.class)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/init_data.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/clear_data.sql")
class LibraryRepositoryTest {

    @Test
    void should_fail_to_log_fetched_LibraryEntity_instance_outside_transactional_context(@Autowired LibraryRepository libraryRepository) {
        var library = libraryRepository.getReferenceById(BNF.id());
        assertThatExceptionOfType(LazyInitializationException.class)
                .isThrownBy(library::toString);
    }

    @Transactional
    @Test
    void should_fail_to_log_fetched_LibraryEntity_instance_due_to_cyclic_dependencies(@Autowired LibraryRepository libraryRepository) {
        var library = libraryRepository.getReferenceById(BNF.id());
        assertThatExceptionOfType(StackOverflowError.class)
                .isThrownBy(library::toString);
    }

}
