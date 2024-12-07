package fr.sdecout.handson.persistence.converters

import fr.sdecout.handson.rest.shared.Isbn
import org.jooq.impl.AbstractConverter

/**
 * # TODO: STEP 7
 *
 * You have probably introduced a lot of calls to `Isbn.compressedValue` and `Isbn.formattedValue` in your queries, with a high risk of inconsistency.
 *
 * @see <a href="https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-forced-types/codegen-database-forced-types-converter/">Qualified converters</a>
 */
class IsbnConverter : AbstractConverter<String, Isbn>(String::class.java, Isbn::class.java) {
    override fun from(databaseObject: String): Isbn = Isbn(databaseObject)
    override fun to(userObject: Isbn): String = userObject.compressedValue
}
