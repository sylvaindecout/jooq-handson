package fr.sdecout.handson.rest.shared;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookField(
        @NotNull @ValidIsbn String isbn,
        @NotBlank String title,
        @Valid @NotEmpty List<AuthorField> authors
) {
}
