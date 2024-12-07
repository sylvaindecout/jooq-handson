package fr.sdecout.handson.rest.author;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorAccess authorAccess;
    private final AuthorSearch authorSearch;
    private final AuthorCreation authorCreation;

    AuthorController(
            AuthorAccess authorAccess,
            AuthorSearch authorSearch,
            AuthorCreation authorCreation
    ) {
        this.authorAccess = authorAccess;
        this.authorSearch = authorSearch;
        this.authorCreation = authorCreation;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable String id) {
        return authorAccess.findAuthor(new AuthorId(id))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public Stream<AuthorSearchResponseItem> searchAuthors(@NotBlank @RequestParam String hint) {
        return authorSearch.searchAuthors(hint);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addAuthor(@Valid @RequestBody AuthorCreationRequest requestBody) {
        var authorId = authorCreation.addAuthor(requestBody.lastName(), requestBody.firstName());
        var location = URI.create("/authors/" + authorId.value());
        return ResponseEntity.created(location).build();
    }

}
