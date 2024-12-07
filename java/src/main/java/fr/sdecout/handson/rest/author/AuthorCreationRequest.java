package fr.sdecout.handson.rest.author;

import jakarta.validation.constraints.NotBlank;

public record AuthorCreationRequest(
        @NotBlank String lastName,
        @NotBlank String firstName
) {
}
