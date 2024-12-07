package fr.sdecout.handson.rest.author;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import fr.sdecout.handson.rest.shared.AuthorField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AuthorSearchResponseItem(
        @JsonUnwrapped @NotNull @Valid AuthorField author
) {
}
