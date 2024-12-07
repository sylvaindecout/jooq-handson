package fr.sdecout.handson.rest;

import fr.sdecout.handson.TestApp;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static fr.sdecout.handson.rest.TestData.CRIME_AND_PUNISHMENT;
import static fr.sdecout.handson.rest.TestData.DOSTOEVSKY;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("Acceptance")
@SpringBootTest(classes = TestApp.class)
@AutoConfigureMockMvc
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/init_data.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/clear_data.sql")
public class AuthorControllerTest {

    @Test
    void should_fail_to_find_unknown_author(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/authors/{id}", "XXX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_find_author(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get("/authors/{id}", DOSTOEVSKY.id()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                    "id": "%s",
                    "lastName": "Dostoevsky",
                    "firstName": "Fyodor",
                    "publishedBooks": [
                        {
                            "isbn": "%s",
                            "title": "Crime and Punishment",
                            "authors": [
                                {
                                    "id":"%s",
                                    "lastName": "Dostoevsky",
                                    "firstName": "Fyodor"
                                }
                            ]
                        }
                    ]
                }""".formatted(DOSTOEVSKY.id(), CRIME_AND_PUNISHMENT.isbn(), DOSTOEVSKY.id()).stripIndent()));
    }

    @Test
    void should_search_authors_matching_hint(@Autowired MockMvc mockMvc) throws Exception {
        var hint = "ostoev";

        mockMvc.perform(post("/authors/search")
                        .queryParam("hint", hint))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [{
                    "id":"%s",
                    "lastName": "Dostoevsky",
                    "firstName": "Fyodor"
                }]""".formatted(DOSTOEVSKY.id()).stripIndent()));
    }

    @Test
    void should_add_author(@Autowired MockMvc mockMvc) throws Exception {
        var lastName = "Herbert";
        var firstName = "Frank";

        mockMvc.perform(post("/authors").contentType(APPLICATION_JSON).content("""
                {
                    "lastName": "%s",
                    "firstName": "%s"
                }""".formatted(lastName, firstName).stripIndent()))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, matchesRegex("/authors/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")));

        mockMvc.perform(post("/authors/search")
                        .queryParam("hint", lastName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].lastName").value(lastName))
                .andExpect(jsonPath("$[0].firstName").value(firstName));
    }

}
