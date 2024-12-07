package fr.sdecout.handson.rest.shared;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LibraryField(
        @NotBlank String id,
        @NotBlank String name,
        @NotNull @Valid AddressField address
) {}
