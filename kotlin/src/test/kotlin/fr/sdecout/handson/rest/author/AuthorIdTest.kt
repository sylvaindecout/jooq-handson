package fr.sdecout.handson.rest.author

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class AuthorIdTest {

    @Test
    fun `should fail to initialize from blank value`() {
        assertThatIllegalArgumentException()
            .isThrownBy { AuthorId("") }
            .withMessage("Author ID must not be blank")
    }

    @Test
    fun `should fail to initialize from value with more than 36 characters`() {
        val value = "1111111111111111111111111111111111111"
        assertThat(value).hasSize(37)

        assertThatIllegalArgumentException()
            .isThrownBy { AuthorId(value) }
            .withMessage("Author ID must not have more than 36 characters")
    }

    @Test
    fun `should display as string`() {
        val value = "111111111111111111111111111111111111"
        assertThat(value).hasSize(36)

        assertThat(AuthorId(value)).hasToString(value)
    }

}
