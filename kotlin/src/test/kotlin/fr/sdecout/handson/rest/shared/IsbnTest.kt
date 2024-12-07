package fr.sdecout.handson.rest.shared

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

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

    @ParameterizedTest
    @ValueSource(strings = ["978-3-16-148410-0", "9783161484100"])
    fun `should expose compressed value`(value: String) {
        val isbn = Isbn(value)

        val compressedValue: String = isbn.compressedValue

        assertThat(compressedValue).isEqualTo("9783161484100")
    }

    @Test
    fun `should display as string`() {
        assertThat(Isbn("9783161484100")).hasToString("978-3-16-148410-0")
    }

}
