package fr.sdecout.handson.rest.shared;

import jakarta.validation.constraints.NotBlank;

public record AuthorField(
        @NotBlank String id,
        @NotBlank String lastName,
        @NotBlank String firstName
) {
}
