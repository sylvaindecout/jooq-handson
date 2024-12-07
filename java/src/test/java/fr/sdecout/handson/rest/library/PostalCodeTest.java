package fr.sdecout.handson.rest.library;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class PostalCodeTest {

    @Test
    void should_fail_to_initialize_from_value_with_less_than_5_digits() {
        var value = "1111";
        assertThat(value).hasSize(4);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new PostalCode(value))
                .withMessage("Postal code must contain 5 numeric digits");
    }

    @Test
    void should_fail_to_initialize_from_value_with_more_than_5_digits() {
        var value = "111111";
        assertThat(value).hasSize(6);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new PostalCode(value))
                .withMessage("Postal code must contain 5 numeric digits");
    }

    @Test
    void should_fail_to_initialize_from_value_including_non_numeric_digits() {
        var value = "1111a";
        assertThat(value).hasSize(5);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new PostalCode(value))
                .withMessage("Postal code must contain 5 numeric digits");
    }

    @Test
    void should_get_department_code() {
        assertThat(new PostalCode("28200").departmentCode()).isEqualTo("28");
    }

    @Test
    void should_display_as_string() {
        var value = "11111";
        assertThat(value).hasSize(5);

        assertThat(new PostalCode(value)).hasToString(value);
    }

}
