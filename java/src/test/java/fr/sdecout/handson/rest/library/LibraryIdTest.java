package fr.sdecout.handson.rest.library;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class LibraryIdTest {

    @Test
    void should_fail_to_initialize_from_blank_value() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new LibraryId(""))
                .withMessage("Library ID must not be blank");
    }

    @Test
    void should_fail_to_initialize_from_value_with_more_than_36_characters() {
        var value = "1111111111111111111111111111111111111";
        assertThat(value).hasSize(37);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new LibraryId(value))
            .withMessage("Library ID must not have more than 36 characters");
    }

    @Test
    void should_display_as_string() {
        var value = "111111111111111111111111111111111111";
        assertThat(value).hasSize(36);

        assertThat(new LibraryId(value)).hasToString(value);
    }

}
