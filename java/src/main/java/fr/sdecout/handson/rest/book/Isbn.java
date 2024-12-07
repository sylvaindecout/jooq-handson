package fr.sdecout.handson.rest.book;

import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public class Isbn {

    private static final Pattern PATTERN = Pattern.compile("[0-9]{13}");

    private final String compressedValue;

    public Isbn(@Nonnull String value) {
        requireNonNull(value);
        this.compressedValue = value.replace("-", "");
        if (!PATTERN.matcher(compressedValue).matches()) { throw new IllegalArgumentException("ISBN must contain 13 numeric digits"); };
    }

    public String compressedValue() {
        return this.compressedValue;
    }

    public String formattedValue() {
        return compressedValue.substring(0, 3) + "-" + compressedValue.substring(3, 4) + "-" + compressedValue.substring(4, 6) + "-" + compressedValue.substring(6, 12) + "-" + compressedValue.substring(12, 13);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Isbn isbn = (Isbn) o;
        return Objects.equals(compressedValue, isbn.compressedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compressedValue);
    }

    @Override
    public String toString() {
        return formattedValue();
    }

}
