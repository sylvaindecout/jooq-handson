package fr.sdecout.handson.rest.shared;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthorField(
        @NotBlank String id,
        @NotBlank String lastName,
        @NotBlank String firstName
) {}
