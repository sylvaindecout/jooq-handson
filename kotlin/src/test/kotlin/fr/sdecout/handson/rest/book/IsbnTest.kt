package fr.sdecout.handson.rest.book

import fr.sdecout.handson.rest.book.Isbn
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class IsbnTest {

    @Test
    fun `should fail to initialize from value with less than 13 digits`() {
        val value = "978316148410"
        assertThat(value).hasSize(12)

        assertThatIllegalArgumentException()
            .isThrownBy { Isbn(value) }
            .withMessage("ISBN must contain 13 numeric digits")
    }

    @Test
    fun `should fail to initialize from value with more than 13 digits`() {
        val value = "97831614841000"
        assertThat(value).hasSize(14)

        assertThatIllegalArgumentException()
            .isThrownBy { Isbn(value) }
            .withMessage("ISBN must contain 13 numeric digits")
    }

    @Test
    fun `should fail to initialize from value including non-numeric characters`() {
        val value = "978316148410a"
        assertThat(value).hasSize(13)

        assertThatIllegalArgumentException()
            .isThrownBy { Isbn(value) }
            .withMessage("ISBN must contain 13 numeric digits")
    }

    @Test
    fun `should display as string`() {
        assertThat(Isbn("9783161484100")).hasToString("978-3-16-148410-0")
    }

}
