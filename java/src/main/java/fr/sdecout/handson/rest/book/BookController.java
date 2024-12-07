package fr.sdecout.handson.rest.book;

import fr.sdecout.handson.rest.author.AuthorId;
import fr.sdecout.handson.rest.shared.Isbn;
import fr.sdecout.handson.rest.shared.ValidIsbn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookAccess bookAccess;
    private final BookSearch bookSearch;
    private final BookUpdate bookUpdate;

    BookController(
            BookAccess bookAccess,
            BookSearch bookSearch,
            BookUpdate bookUpdate
    ) {
        this.bookAccess = bookAccess;
        this.bookSearch = bookSearch;
        this.bookUpdate = bookUpdate;
    }

    @GetMapping(value = "/{isbn}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponse> getBook(@ValidIsbn @PathVariable String isbn) {
        return bookAccess.findBook(new Isbn(isbn))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public Stream<BookSearchResponseItem> searchBooks(@NotBlank @RequestParam String hint) {
        return bookSearch.searchBooks(hint);
    }

    @PutMapping(value = "/{isbn}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void addOrUpdateBook(@ValidIsbn @PathVariable String isbn, @Valid @RequestBody BookUpdateRequest requestBody) {
        bookUpdate.save(
                new Isbn(isbn),
                requestBody.title(),
                requestBody.authors().stream().map(AuthorId::new).toList()
        );
    }

}
