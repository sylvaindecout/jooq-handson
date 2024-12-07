package fr.sdecout.handson.rest

import fr.sdecout.handson.TestApp
import fr.sdecout.handson.rest.shared.Isbn
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Tag("Acceptance")
@SpringBootTest(classes = [TestApp::class])
@AutoConfigureMockMvc
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = ["/init_data.sql"])
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = ["/clear_data.sql"])
class BookControllerTest {

    @Test
    fun `should fail to find unknown book`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(get("/books/{isbn}", "0000000000000"))
            .andExpect(status().isNotFound())
    }

    @Test
    fun `should find book available at several libraries`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(get("/books/{isbn}", CRIME_AND_PUNISHMENT.isbn))
            .andExpect(status().isOk())
            .andExpect(
                content().json(
                    """{
                        "isbn": "${CRIME_AND_PUNISHMENT.isbn}",
                        "title": "Crime and Punishment",
                        "authors": [
                            {
                                "id":"${DOSTOEVSKY.id}",
                                "lastName": "Dostoevsky",
                                "firstName": "Fyodor"
                            }
                        ],
                        "availability": [
                            {
                                "id": "${BNF.id}",
                                "name": "BNF",
                                "address": {
                                    "line1": "Quai François-Mauriac",
                                    "line2": "Cedex 13",
                                    "postalCode": "75706",
                                    "city": "Paris"
                                }
                            },
                            {
                                "id": "${VILLON.id}",
                                "name": "Bibliothèque François Villon",
                                "address": {
                                    "line1": "81, boulevard de la Villette",
                                    "postalCode": "75010",
                                    "city": "Paris"
                                }
                            }
                        ]
                    }""".trimIndent()
                )
            )
    }

    @Test
    fun `should find book with several authors`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(get("/books/{isbn}", LES_SENS_DU_VOTE.isbn))
            .andExpect(status().isOk())
            .andExpect(
                content().json(
                    """{
                        "isbn": "${LES_SENS_DU_VOTE.isbn}",
                        "title": "Les sens du vote",
                        "authors": [
                            {
                                "id":"${CLEMENTINE_BERJAUD.id}",
                                "lastName": "Berjaud",
                                "firstName": "Clémentine"
                            },
                            {
                                "id":"${AF_TAICLET.id}",
                                "lastName": "Taiclet",
                                "firstName": "Anne-France"
                            },
                            {
                                "id":"${THIBAUD_BONCOURT.id}",
                                "lastName": "Boncourt",
                                "firstName": "Thibaud"
                            },
                            {
                                "id":"${JULIEN_FRETEL.id}",
                                "lastName": "Fretel",
                                "firstName": "Julien"
                            },
                            {
                                "id":"${DANIEL_GAXIE.id}",
                                "lastName": "Gaxie",
                                "firstName": "Daniel"
                            }
                        ],
                        "availability": []
                    }""".trimIndent()
                )
            )
    }

    @Test
    fun `should search books matching hint`(@Autowired mockMvc: MockMvc) {
        val hint = "rime and punish"

        mockMvc.perform(post("/books/search")
                .queryParam("hint", hint))
            .andExpect(status().isOk())
            .andExpect(
                content().json(
                    """[{
                        "isbn": "${CRIME_AND_PUNISHMENT.isbn}",
                        "title": "Crime and Punishment",
                        "authors": [
                            {
                                "id":"${DOSTOEVSKY.id}",
                                "lastName": "Dostoevsky",
                                "firstName": "Fyodor"
                            }
                        ]
                    }]""".trimIndent()
                )
            )
    }

    @Test
    fun `should add book`(@Autowired mockMvc: MockMvc) {
        val isbn = Isbn(THE_IDIOT.isbn!!)
        val title = THE_IDIOT.title!!

        mockMvc.perform(
            put("/books/{isbn}", isbn)
                .contentType(APPLICATION_JSON).content(
                    """{
                        "title": "$title",
                        "authors": ["${DOSTOEVSKY.id}"]
                    }""".trimMargin()
                )
        )
            .andExpect(status().isNoContent())

        mockMvc.perform(get("/books/{isbn}", isbn))
            .andExpect(status().isOk())
            .andExpect(
                content().json(
                    """{
                        "isbn": "${THE_IDIOT.isbn}",
                        "title": "${THE_IDIOT.title}",
                        "authors": [
                            {
                                "id":"${DOSTOEVSKY.id}",
                                "lastName": "Dostoevsky",
                                "firstName": "Fyodor"
                            }
                        ],
                        "availability": []
                    }""".trimIndent()
                )
            )
    }

}
