package fr.sdecout.handson.persistence.author

import fr.sdecout.handson.rest.author.*
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@Transactional
class DbAuthorAdapter(
    private val authorRepository: AuthorRepository,
) : AuthorAccess, AuthorCreation, AuthorSearch {

    override fun findAuthor(id: AuthorId): AuthorResponse? = authorRepository.findByIdOrNull(id.value)
        ?.toAuthorResponse()

    override fun searchAuthors(hint: String): List<AuthorSearchResponseItem> = authorRepository
        .findByLastNameLikeIgnoringCase("%$hint%")
        .map { it.toAuthorSearchResponseItem() }

    override fun addAuthor(lastName: String, firstName: String): AuthorId = AuthorId.next().also {
        authorRepository.save(
            AuthorEntity(
                id = AuthorId.next().value,
                lastName = lastName,
                firstName = firstName,
                publishedBooks = mutableListOf()
            )
        )
    }

}
