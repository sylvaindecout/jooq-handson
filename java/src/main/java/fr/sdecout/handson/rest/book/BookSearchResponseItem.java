package fr.sdecout.handson.rest.book;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import fr.sdecout.handson.rest.shared.BookField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BookSearchResponseItem(
        @JsonUnwrapped @NotNull @Valid BookField book
) {
}
