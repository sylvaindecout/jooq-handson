package fr.sdecout.handson.rest.author

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/authors")
class AuthorController(
    private val authorAccess: AuthorAccess,
    private val authorSearch: AuthorSearch,
    private val authorCreation: AuthorCreation,
) {

    @GetMapping("/{id}", produces = [APPLICATION_JSON_VALUE])
    fun getAuthor(@PathVariable id: String): ResponseEntity<AuthorResponse> = authorAccess.findAuthor(AuthorId(id))
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @PostMapping("/search", produces = [APPLICATION_JSON_VALUE])
    fun searchAuthors(@NotBlank @RequestParam hint: String): List<AuthorSearchResponseItem> =
        authorSearch.searchAuthors(hint)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    fun addAuthor(@Valid @RequestBody requestBody: AuthorCreationRequest): ResponseEntity<Void> = authorCreation
        .addAuthor(requestBody.lastName!!, requestBody.firstName!!)
        .let { URI.create("/authors/${it.value}") }
        .let { ResponseEntity.created(it).build() }

}
