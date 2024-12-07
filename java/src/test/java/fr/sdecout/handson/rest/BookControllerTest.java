package fr.sdecout.handson.rest;

import fr.sdecout.handson.TestApp;
import fr.sdecout.handson.rest.shared.Isbn;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static fr.sdecout.handson.rest.TestData.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("Acceptance")
@SpringBootTest(classes = TestApp.class)
@AutoConfigureMockMvc
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/init_data.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/clear_data.sql")
public class BookControllerTest {

    @Test
    void should_fail_to_find_unknown_book(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/books/{isbn}", "0000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_find_book_available_at_several_libraries(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/books/{isbn}", CRIME_AND_PUNISHMENT.isbn()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "isbn": "%s",
                            "title": "Crime and Punishment",
                            "authors": [
                                {
                                    "id":"%s",
                                    "lastName": "Dostoevsky",
                                    "firstName": "Fyodor"
                                }
                            ],
                            "availability": [
                                {
                                    "id": "%s",
                                    "name": "BNF",
                                    "address": {
                                        "line1": "Quai François-Mauriac",
                                        "line2": "Cedex 13",
                                        "postalCode": "75706",
                                        "city": "Paris"
                                    }
                                },
                                {
                                    "id": "%s",
                                    "name": "Bibliothèque François Villon",
                                    "address": {
                                        "line1": "81, boulevard de la Villette",
                                        "postalCode": "75010",
                                        "city": "Paris"
                                    }
                                }
                            ]
                        }""".formatted(CRIME_AND_PUNISHMENT.isbn(), DOSTOEVSKY.id(), BNF.id(), VILLON.id()).stripIndent()));
    }

    @Test
    void should_find_book_with_several_authors(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/books/{isbn}", LES_SENS_DU_VOTE.isbn()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "isbn": "%s",
                            "title": "Les sens du vote",
                            "authors": [
                                {
                                    "id":"%s",
                                    "lastName": "Berjaud",
                                    "firstName": "Clémentine"
                                },
                                {
                                    "id":"%s",
                                    "lastName": "Taiclet",
                                    "firstName": "Anne-France"
                                },
                                {
                                    "id":"%s",
                                    "lastName": "Boncourt",
                                    "firstName": "Thibaud"
                                },
                                {
                                    "id":"%s",
                                    "lastName": "Fretel",
                                    "firstName": "Julien"
                                },
                                {
                                    "id":"%s",
                                    "lastName": "Gaxie",
                                    "firstName": "Daniel"
                                }
                            ],
                            "availability": []
                        }""".formatted(LES_SENS_DU_VOTE.isbn(), CLEMENTINE_BERJAUD.id(), AF_TAICLET.id(), THIBAUD_BONCOURT.id(), JULIEN_FRETEL.id(), DANIEL_GAXIE.id()).stripIndent()));
    }

    @Test
    void should_search_books_matching_hint(@Autowired MockMvc mockMvc) throws Exception {
        var hint = "rime and punish";

        mockMvc.perform(post("/books/search")
                        .queryParam("hint", hint))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                            "isbn": "%s",
                            "title": "Crime and Punishment",
                            "authors": [
                                {
                                    "id":"%s",
                                    "lastName": "Dostoevsky",
                                    "firstName": "Fyodor"
                                }
                            ]
                        }]""".formatted(CRIME_AND_PUNISHMENT.isbn(), DOSTOEVSKY.id()).stripIndent()));
    }

    @Test
    void should_add_book(@Autowired MockMvc mockMvc) throws Exception {
        var isbn = new Isbn(THE_IDIOT.isbn());
        var title = THE_IDIOT.title();

        mockMvc.perform(put("/books/{isbn}", isbn)
                        .contentType(APPLICATION_JSON).content("""
                                {
                                    "title": "%s",
                                    "authors": ["%s"]
                                }""".formatted(title, DOSTOEVSKY.id()).stripIndent()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books/{isbn}", isbn))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "isbn": "%s",
                            "title": "%s",
                            "authors": [
                                {
                                    "id":"%s",
                                    "lastName": "Dostoevsky",
                                    "firstName": "Fyodor"
                                }
                            ],
                            "availability": []
                        }""".formatted(THE_IDIOT.isbn(), THE_IDIOT.title(), DOSTOEVSKY.id()).stripIndent()));
    }

}
