package fr.sdecout.handson.rest.book;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

record BookUpdateRequest(
        @NotBlank String title,
        @Valid @NotEmpty List<String> authors
) {}
