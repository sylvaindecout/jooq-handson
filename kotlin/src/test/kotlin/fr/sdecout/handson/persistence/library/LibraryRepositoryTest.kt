package fr.sdecout.handson.persistence.library

import fr.sdecout.handson.TestApp
import fr.sdecout.handson.rest.BNF
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.hibernate.LazyInitializationException
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import org.springframework.transaction.annotation.Transactional

@Tag("Integration")
@SpringBootTest(classes = [TestApp::class])
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = ["/init_data.sql"])
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = ["/clear_data.sql"])
class LibraryRepositoryTest {

    @Test
    fun `should fail to log fetched LibraryEntity instance outside transactional context`(@Autowired libraryRepository: LibraryRepository) {
        val library = libraryRepository.findByIdOrNull(BNF.id)
        assertThatExceptionOfType(LazyInitializationException::class.java)
            .isThrownBy { library.toString() }
    }

    @Transactional
    @Test
    fun `should fail to log fetched LibraryEntity instance due to cyclic dependencies`(@Autowired libraryRepository: LibraryRepository) {
        val library = libraryRepository.findByIdOrNull(BNF.id)
        assertThatExceptionOfType(StackOverflowError::class.java)
            .isThrownBy { library.toString() }
    }

}
