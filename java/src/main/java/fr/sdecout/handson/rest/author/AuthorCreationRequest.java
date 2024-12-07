package fr.sdecout.handson.rest.author;

import jakarta.validation.constraints.NotBlank;

record AuthorCreationRequest(
        @NotBlank String lastName,
        @NotBlank String firstName
) {}
