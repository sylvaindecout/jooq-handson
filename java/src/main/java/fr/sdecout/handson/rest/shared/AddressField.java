package fr.sdecout.handson.rest.shared;

import jakarta.validation.constraints.NotBlank;

public record AddressField(
        @NotBlank String line1,
        String line2,
        @NotBlank String postalCode,
        @NotBlank String city
) {
}
