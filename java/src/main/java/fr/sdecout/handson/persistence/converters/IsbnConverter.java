package fr.sdecout.handson.persistence.converters;

import fr.sdecout.handson.rest.shared.Isbn;
import org.jooq.impl.AbstractConverter;

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
