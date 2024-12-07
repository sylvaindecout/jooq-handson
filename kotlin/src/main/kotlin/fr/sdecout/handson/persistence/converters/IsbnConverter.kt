package fr.sdecout.handson.persistence.converters

import fr.sdecout.handson.rest.shared.Isbn
import org.jooq.impl.AbstractConverter

/**
 * # TODO: Step 5
 *
 * You have probably introduced a lot of calls to [Isbn.compressedValue] or [Isbn.formattedValue] in your commands and
 * queries.
 * On top of being verbose, it comes with a high risk of inconsistency, which could lead to bugs.
 *
 * @see <a href="https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-forced-types/codegen-database-forced-types-converter/">Qualified converters</a>
 */
class IsbnConverter : AbstractConverter<String, Isbn>(String::class.java, Isbn::class.java) {
    override fun from(databaseObject: String): Isbn = Isbn(databaseObject)
    override fun to(userObject: Isbn): String = userObject.compressedValue
}
