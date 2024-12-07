package fr.sdecout.handson.rest.author;

import fr.sdecout.handson.rest.shared.BookField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record AuthorResponse(
        @NotBlank String id,
        @NotBlank String lastName,
        @NotBlank String firstName,
        @Valid List<BookField> publishedBooks
) {
}
