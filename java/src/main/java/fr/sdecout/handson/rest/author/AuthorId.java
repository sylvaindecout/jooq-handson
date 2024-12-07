package fr.sdecout.handson.rest.author;

import jakarta.annotation.Nonnull;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record AuthorId(@Nonnull String value) {
    public AuthorId {
        requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Author ID must not be blank");
        }
        if (value.length() > 36) {
            throw new IllegalArgumentException("Author ID must not have more than 36 characters");
        }
    }

    public static AuthorId next() {
        return new AuthorId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
