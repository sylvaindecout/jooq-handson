package fr.sdecout.handson.rest.library

import fr.sdecout.handson.rest.author.AuthorId
import fr.sdecout.handson.rest.book.Isbn
import fr.sdecout.handson.rest.shared.ValidIsbn
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI

@RestController
@RequestMapping("/libraries")
class LibraryController(
    private val libraryAccess: LibraryAccess,
    private val librarySearch: LibrarySearch,
    private val libraryCreation: LibraryCreation,
    private val bookCollectionUpdate: BookCollectionUpdate,
) {

    @GetMapping("/{id}", produces = [APPLICATION_JSON_VALUE])
    fun getLibrary(@PathVariable id: String): ResponseEntity<LibraryResponse> = libraryAccess.findLibrary(LibraryId(id))
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @PostMapping("/search", produces = [APPLICATION_JSON_VALUE])
    fun searchLibraries(
        @RequestParam(required = false) postalCode: String?,
        @ValidIsbn @RequestParam(required = false) isbn: String?
    ): List<LibrarySearchResponseItem> = when {
        postalCode != null -> librarySearch.searchLibrariesClosestTo(PostalCode(postalCode))
        isbn != null -> librarySearch.searchLibrariesWithBookAvailable(Isbn(isbn))
        else -> throw ResponseStatusException(BAD_REQUEST, "At least one criterion must be provided")
    }

    @PostMapping("/count", produces = [APPLICATION_JSON_VALUE])
    fun countLibraries(@NotBlank @RequestParam author: String?): Long = librarySearch.countLibrariesWithBooksBy(AuthorId(author!!))

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    fun addLibrary(@Valid @RequestBody requestBody: LibraryCreationRequest): ResponseEntity<Void> = libraryCreation
        .addLibrary(requestBody.name!!, requestBody.address!!)
        .let { URI.create("/libraries/${it.value}") }
        .let { ResponseEntity.created(it).build() }

    @PostMapping("/{libraryId}/books")
    @ResponseStatus(NO_CONTENT)
    fun addBook(@PathVariable libraryId: String, @NotNull @ValidIsbn @RequestParam isbn: String) {
        bookCollectionUpdate.addBook(LibraryId(libraryId), Isbn(isbn))
    }

}
