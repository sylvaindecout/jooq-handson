package fr.sdecout.handson.rest.library;

import fr.sdecout.handson.rest.shared.AddressField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LibraryResponse(
        @NotBlank String id,
        @NotBlank String name,
        @NotNull @Valid AddressField address
) {}
