package fr.sdecout.handson.rest.author;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class AuthorIdTest {

    @Test
    void should_fail_to_initialize_from_blank_value() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new AuthorId(""))
                .withMessage("Author ID must not be blank");
    }

    @Test
    void should_fail_to_initialize_from_value_with_more_than_36_characters() {
        var value = "1111111111111111111111111111111111111";
        assertThat(value).hasSize(37);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new AuthorId(value))
                .withMessage("Author ID must not have more than 36 characters");
    }

    @Test
    void should_display_as_string() {
        var value = "111111111111111111111111111111111111";
        assertThat(value).hasSize(36);

        assertThat(new AuthorId(value)).hasToString(value);
    }

}
