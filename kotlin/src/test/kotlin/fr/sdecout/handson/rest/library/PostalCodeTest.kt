package fr.sdecout.handson.rest.library

import fr.sdecout.handson.rest.library.PostalCode
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class PostalCodeTest {

    @Test
    fun `should fail to initialize from value with less than 5 digits`() {
        val value = "1111"
        assertThat(value).hasSize(4)

        assertThatIllegalArgumentException()
            .isThrownBy { PostalCode(value) }
            .withMessage("Postal code must contain 5 numeric digits")
    }

    @Test
    fun `should fail to initialize from value with more than 5 digits`() {
        val value = "111111"
        assertThat(value).hasSize(6)

        assertThatIllegalArgumentException()
            .isThrownBy { PostalCode(value) }
            .withMessage("Postal code must contain 5 numeric digits")
    }

    @Test
    fun `should fail to initialize from value including non-numeric digits`() {
        val value = "1111a"
        assertThat(value).hasSize(5)

        assertThatIllegalArgumentException()
            .isThrownBy { PostalCode(value) }
            .withMessage("Postal code must contain 5 numeric digits")
    }

    @Test
    fun `should get department code`() {
        assertThat(PostalCode("28200").departmentCode).isEqualTo("28")
    }

    @Test
    fun `should display as string`() {
        val value = "11111"
        assertThat(value).hasSize(5)

        assertThat(PostalCode(value)).hasToString(value)
    }

}
