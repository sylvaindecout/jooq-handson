package fr.sdecout.handson.rest.library;

import jakarta.annotation.Nonnull;

import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public record PostalCode(@Nonnull String value) {

    private static final Pattern PATTERN = Pattern.compile("[0-9]{5}");

    public PostalCode {
        requireNonNull(value);
        if (!PATTERN.matcher(value).matches()) { throw new IllegalArgumentException("Postal code must contain 5 numeric digits"); };
    }

    public String departmentCode() {
        return value.substring(0, 2);
    }

    @Override
    public String toString() {
        return value;
    }
}
