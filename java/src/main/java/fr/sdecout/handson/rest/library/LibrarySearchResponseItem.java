package fr.sdecout.handson.rest.library;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import fr.sdecout.handson.rest.shared.LibraryField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LibrarySearchResponseItem(
        @JsonUnwrapped @NotNull @Valid LibraryField library
) {
}
