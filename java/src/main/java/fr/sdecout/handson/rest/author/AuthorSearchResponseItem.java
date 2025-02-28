package fr.sdecout.handson.rest.author;

import jakarta.validation.constraints.NotBlank;

public record AuthorSearchResponseItem(
        @NotBlank String id,
        @NotBlank String lastName,
        @NotBlank String firstName
) {
}
