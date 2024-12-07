package fr.sdecout.handson.rest;

import fr.sdecout.handson.TestApp;
import fr.sdecout.handson.rest.book.Isbn;
import fr.sdecout.handson.rest.library.LibraryId;
import fr.sdecout.handson.rest.library.PostalCode;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static fr.sdecout.handson.rest.TestData.*;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("Acceptance")
@SpringBootTest(classes = TestApp.class)
@AutoConfigureMockMvc
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/init_data.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/clear_data.sql")
public class LibraryControllerTest {

    @Test
    void should_fail_to_find_unknown_library(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/libraries/{id}", "XXX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_find_library(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/libraries/{id}", BNF.id()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                    "id":"%s",
                    "name": "BNF",
                    "address": {
                        "line1": "Quai François-Mauriac",
                        "line2": "Cedex 13",
                        "postalCode": "75706",
                        "city": "Paris"
                    }
                }""".formatted(BNF.id()).stripIndent()));
    }

    @Test
    void should_search_libraries_closest_to_postal_code(@Autowired MockMvc mockMvc) throws Exception {
        var postalCode = new PostalCode("75000");

        mockMvc.perform(post("/libraries/search")
                        .queryParam("postalCode", postalCode.value()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                [
                    {
                        "id":"%s",
                        "name": "BNF",
                        "address": {
                            "line1": "Quai François-Mauriac",
                            "line2": "Cedex 13",
                            "postalCode": "75706",
                            "city": "Paris"
                        }
                    }, {
                        "id":"%s",
                        "name": "Bibliothèque François Villon",
                        "address": {
                            "line1": "81, boulevard de la Villette",
                            "postalCode": "75010",
                            "city": "Paris"
                        }
                    }
                ]""".formatted(BNF.id(), VILLON.id()).stripIndent()));
    }

    @Test
    void should_search_libraries_with_book_available(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/libraries/search")
                        .queryParam("isbn", CRIME_AND_PUNISHMENT.isbn()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                [
                    {
                        "id":"%s",
                        "name": "BNF",
                        "address": {
                            "line1": "Quai François-Mauriac",
                            "line2": "Cedex 13",
                            "postalCode": "75706",
                            "city": "Paris"
                        }
                    }, {
                        "id":"%s",
                        "name": "Bibliothèque François Villon",
                        "address": {
                            "line1": "81, boulevard de la Villette",
                            "postalCode": "75010",
                            "city": "Paris"
                        }
                    }
                ]""".formatted(BNF.id(), VILLON.id()).stripIndent()));
    }

    @Test
    void should_fail_to_search_libraries_if_no_criterion_is_provided(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/libraries/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_count_libraries_with_books_from_author_available(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/libraries/count")
                        .queryParam("author", DOSTOEVSKY.id()))
                .andExpect(status().isOk())
                .andExpect(content().json("2"));
    }

    @Test
    void should_fail_to_count_libraries_with_books_from_author_available_if_author_ID_is_not_provided(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/libraries/count"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_library(@Autowired MockMvc mockMvc) throws Exception {
        var name = "Médiathèque de Châteaudun";

        mockMvc.perform(post("/libraries")
                        .contentType(APPLICATION_JSON).content("""
                    {
                        "name": "%s",
                        "address": {
                            "line1": "36, boulevard Grindelle",
                            "postalCode": "28200",
                            "city": "Châteaudun"
                        }
                    }""".formatted(name).stripIndent()))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, matchesRegex("/libraries/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")));

        mockMvc.perform(post("/libraries/search")
                        .queryParam("postalCode", "28200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].address.line1").value("36, boulevard Grindelle"))
                .andExpect(jsonPath("$[0].address.line2").value(nullValue()))
                .andExpect(jsonPath("$[0].address.postalCode").value("28200"))
                .andExpect(jsonPath("$[0].address.city").value("Châteaudun"));
    }

    @Test
    void should_add_book(@Autowired MockMvc mockMvc) throws Exception {
        var libraryId = new LibraryId(BNF.id());
        var isbn = new Isbn(LES_SENS_DU_VOTE.isbn());

        mockMvc.perform(post("/libraries/{libraryId}/books", libraryId)
                        .queryParam("isbn", isbn.formattedValue()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books/{isbn}", LES_SENS_DU_VOTE.isbn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availability.size()").value(1))
                .andExpect(jsonPath("$.availability[0].id").value(BNF.id()))
                .andExpect(jsonPath("$.availability[0].name").value(BNF.name()))
                .andExpect(jsonPath("$.availability[0].address.line1").value(BNF.address().line1()))
            .andExpect(jsonPath("$.availability[0].address.line2").value(BNF.address().line2()))
            .andExpect(jsonPath("$.availability[0].address.postalCode").value(BNF.address().postalCode()))
            .andExpect(jsonPath("$.availability[0].address.city").value(BNF.address().city()));
    }

}
