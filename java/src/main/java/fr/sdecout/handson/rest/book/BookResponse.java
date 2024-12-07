package fr.sdecout.handson.rest.book;

import fr.sdecout.handson.rest.shared.AuthorField;
import fr.sdecout.handson.rest.shared.LibraryField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record BookResponse(
        @NotBlank String isbn,
        @NotBlank String title,
        @Valid @NotEmpty List<AuthorField> authors,
        @Valid List<LibraryField> availability
) {
}
