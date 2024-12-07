package fr.sdecout.handson.persistence.converters;

import fr.sdecout.handson.rest.shared.Isbn;
import org.jooq.impl.AbstractConverter;

/**
 * <h1>TODO: Step 7</h1>
 * <p>
 * You have probably introduced a lot of calls to {@link Isbn#compressedValue()} or {@link Isbn#formattedValue()} in
 * your commands and queries.
 * On top of being verbose, it comes with a high risk of inconsistency, which could lead to bugs.
 * </p>
 *
 * @see <a href="https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-forced-types/codegen-database-forced-types-converter/">Qualified converters</a>
 */
public final class IsbnConverter extends AbstractConverter<String, Isbn> {

    public IsbnConverter() {
        super(String.class, Isbn.class);
    }

    @Override
    public Isbn from(String databaseObject) {
        return new Isbn(databaseObject);
    }

    @Override
    public String to(Isbn userObject) {
        return userObject.compressedValue();
    }

}
