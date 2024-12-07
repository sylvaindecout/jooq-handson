package fr.sdecout.handson.rest.library;

import jakarta.annotation.Nonnull;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record LibraryId(@Nonnull String value) {
    public LibraryId {
        requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Library ID must not be blank");
        }
        if (value.length() > 36) {
            throw new IllegalArgumentException("Library ID must not have more than 36 characters");
        }
    }

    public static LibraryId next() {
        return new LibraryId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
