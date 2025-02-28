package fr.sdecout.handson.rest.book;

import fr.sdecout.handson.rest.shared.AuthorField;
import fr.sdecout.handson.rest.shared.ValidIsbn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookSearchResponseItem(
        @NotNull @ValidIsbn String isbn,
        @NotBlank String title,
        @Valid @NotEmpty List<AuthorField> authors
) {
}
