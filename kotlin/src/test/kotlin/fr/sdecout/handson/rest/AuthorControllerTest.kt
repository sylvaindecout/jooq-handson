package fr.sdecout.handson.rest

import fr.sdecout.handson.TestApp
import org.hamcrest.Matchers.matchesRegex
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@Tag("Acceptance")
@SpringBootTest(classes = [TestApp::class])
@AutoConfigureMockMvc
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = ["/init_data.sql"])
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = ["/clear_data.sql"])
class AuthorControllerTest {

    @Test
    fun `should fail to find unknown author`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(get("/authors/{id}", "XXX"))
            .andExpect(status().isNotFound())
    }

    @Test
    fun `should find author`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(get("/authors/{id}", DOSTOEVSKY.id))
            .andExpect(status().isOk())
            .andExpect(content().json("""{
                "id": "${DOSTOEVSKY.id}",
                "lastName": "Dostoevsky",
                "firstName": "Fyodor",
                "publishedBooks": [
                    {
                        "isbn": "${CRIME_AND_PUNISHMENT.isbn}",
                        "title": "Crime and Punishment",
                        "authors": [
                            {
                                "id":"${DOSTOEVSKY.id}",
                                "lastName": "Dostoevsky",
                                "firstName": "Fyodor"
                            }
                        ]
                    }
                ]
            }""".trimIndent()))
    }

    @Test
    fun `should search authors matching hint`(@Autowired mockMvc: MockMvc) {
        val hint = "ostoev"

        mockMvc.perform(post("/authors/search")
                .queryParam("hint", hint))
            .andExpect(status().isOk())
            .andExpect(content().json("""[{
                "id":"${DOSTOEVSKY.id}",
                "lastName": "Dostoevsky",
                "firstName": "Fyodor"
            }]""".trimIndent()))
    }

    @Test
    fun `should add author`(@Autowired mockMvc: MockMvc) {
        val lastName = "Herbert"
        val firstName = "Frank"

        mockMvc.perform(post("/authors").contentType(APPLICATION_JSON).content("""{
                    "lastName": "$lastName",
                    "firstName": "$firstName"
                }""".trimMargin()))
            .andExpect(status().isCreated())
            .andExpect(header().string(LOCATION, matchesRegex("/authors/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))

        mockMvc.perform(post("/authors/search")
                .queryParam("hint", lastName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].lastName").value(lastName))
            .andExpect(jsonPath("$[0].firstName").value(firstName))
    }

}
