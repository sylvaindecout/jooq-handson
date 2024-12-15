package fr.sdecout.handson.persistence.converters

import fr.sdecout.handson.rest.shared.Isbn
import org.jooq.impl.AbstractConverter

class IsbnConverter : AbstractConverter<String, Isbn>(String::class.java, Isbn::class.java) {
    override fun from(databaseObject: String): Isbn = Isbn(databaseObject)
    override fun to(userObject: Isbn): String = userObject.compressedValue
}
