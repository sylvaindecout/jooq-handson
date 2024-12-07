package fr.sdecout.handson.rest.book;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class IsbnTest {

    @Test
    void should_fail_to_initialize_from_value_with_less_than_13_digits() {
        var value = "978316148410";
        assertThat(value).hasSize(12);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Isbn(value))
            .withMessage("ISBN must contain 13 numeric digits");
    }

    @Test
    void should_fail_to_initialize_from_value_with_more_than_13_digits() {
        var value = "97831614841000";
        assertThat(value).hasSize(14);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Isbn(value))
                .withMessage("ISBN must contain 13 numeric digits");
    }

    @Test
    void should_fail_to_initialize_from_value_including_non_numeric_characters() {
        var value = "978316148410a";
        assertThat(value).hasSize(13);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Isbn(value))
            .withMessage("ISBN must contain 13 numeric digits");
    }

    @Test
    void should_display_as_string() {
        assertThat(new Isbn("9783161484100")).hasToString("978-3-16-148410-0");
    }

}
