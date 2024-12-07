package fr.sdecout.handson.rest.shared;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * <h1>TODO: Step 6</h1>
 * <p>
 * Uncomment <code>003.add-book-release-year.yaml</code> migration script in <code>db.changelog-master.yaml</code> in
 * order to add a new field.
 * </p>
 * <p>Note that the field is not available at once. You first need to build once.</p>
 * <p>
 * Check <code>:heavy_plus_sign: Integrate jOOQ</code> commit in order to understand how code generation is automated
 * with <a href="https://github.com/etiennestuder/gradle-jooq-plugin">gradle-jooq-plugin</a>.
 * You can have a look at <code>source-generation.md</code> to understand the steps of the generation.
 * </p>
 */
public record BookField(
        @NotNull @ValidIsbn String isbn,
        @NotBlank String title,
        @Valid @NotEmpty List<AuthorField> authors
) {
}
