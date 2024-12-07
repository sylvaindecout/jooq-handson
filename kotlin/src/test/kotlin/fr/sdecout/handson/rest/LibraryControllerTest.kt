package fr.sdecout.handson.rest

import fr.sdecout.handson.TestApp
import fr.sdecout.handson.rest.library.LibraryId
import fr.sdecout.handson.rest.library.PostalCode
import fr.sdecout.handson.rest.shared.Isbn
import org.hamcrest.Matchers.matchesRegex
import org.hamcrest.Matchers.nullValue
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
class LibraryControllerTest {

    @Test
    fun `should fail to find unknown library`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(get("/libraries/{id}", "XXX"))
            .andExpect(status().isNotFound())
    }

    @Test
    fun `should find library`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(get("/libraries/{id}", BNF.id))
            .andExpect(status().isOk())
            .andExpect(
                content().json(
                    """{
                        "id":"${BNF.id}",
                        "name": "BNF",
                        "address": {
                            "line1": "Quai François-Mauriac",
                            "line2": "Cedex 13",
                            "postalCode": "75706",
                            "city": "Paris"
                        }
                    }""".trimIndent()
                )
            )
    }

    @Test
    fun `should search libraries closest to postal code`(@Autowired mockMvc: MockMvc) {
        val postalCode = PostalCode("75000")

        mockMvc.perform(post("/libraries/search")
                .queryParam("postalCode", postalCode.value))
            .andExpect(status().isOk())
            .andExpect(
                content().json(
                    """[
                        {
                            "id":"${BNF.id}",
                            "name": "BNF",
                            "address": {
                                "line1": "Quai François-Mauriac",
                                "line2": "Cedex 13",
                                "postalCode": "75706",
                                "city": "Paris"
                            }
                        }, {
                            "id":"${VILLON.id}",
                            "name": "Bibliothèque François Villon",
                            "address": {
                                "line1": "81, boulevard de la Villette",
                                "postalCode": "75010",
                                "city": "Paris"
                            }
                        }
                    ]""".trimIndent()
                )
            )
    }

    @Test
    fun `should search libraries with book available`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(post("/libraries/search")
                .queryParam("isbn", CRIME_AND_PUNISHMENT.isbn))
            .andExpect(status().isOk())
            .andExpect(
                content().json(
                    """[
                        {
                            "id":"${BNF.id}",
                            "name": "BNF",
                            "address": {
                                "line1": "Quai François-Mauriac",
                                "line2": "Cedex 13",
                                "postalCode": "75706",
                                "city": "Paris"
                            }
                        }, {
                            "id":"${VILLON.id}",
                            "name": "Bibliothèque François Villon",
                            "address": {
                                "line1": "81, boulevard de la Villette",
                                "postalCode": "75010",
                                "city": "Paris"
                            }
                        }
                    ]""".trimIndent()
                )
            )
    }

    @Test
    fun `should fail to search libraries if no criterion is provided`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(post("/libraries/search"))
            .andExpect(status().isBadRequest())
    }

    @Test
    fun `should count libraries with books from author available`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(post("/libraries/count")
                .queryParam("author", DOSTOEVSKY.id))
            .andExpect(status().isOk())
            .andExpect(content().json("2"))
    }

    @Test
    fun `should fail to count libraries with books from author available if author ID is not provided`(@Autowired mockMvc: MockMvc) {
        mockMvc.perform(post("/libraries/count"))
            .andExpect(status().isBadRequest())
    }

    @Test
    fun `should add library`(@Autowired mockMvc: MockMvc) {
        val name = "Médiathèque de Châteaudun"

        mockMvc.perform(
            post("/libraries")
                .contentType(APPLICATION_JSON).content(
                    """{
                        "name": "$name",
                        "address": {
                            "line1": "36, boulevard Grindelle",
                            "postalCode": "28200",
                            "city": "Châteaudun"
                        }
                    }""".trimMargin()
                )
        )
            .andExpect(status().isCreated())
            .andExpect(
                header().string(
                    LOCATION,
                    matchesRegex("/libraries/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
                )
            )

        mockMvc.perform(post("/libraries/search")
                .queryParam("postalCode", "28200"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].name").value(name))
            .andExpect(jsonPath("$[0].address.line1").value("36, boulevard Grindelle"))
            .andExpect(jsonPath("$[0].address.line2").value(nullValue()))
            .andExpect(jsonPath("$[0].address.postalCode").value("28200"))
            .andExpect(jsonPath("$[0].address.city").value("Châteaudun"))
    }

    @Test
    fun `should add book`(@Autowired mockMvc: MockMvc) {
        val libraryId = LibraryId(BNF.id!!)
        val isbn = Isbn(LES_SENS_DU_VOTE.isbn!!)

        mockMvc.perform(post("/libraries/{libraryId}/books", libraryId)
                .queryParam("isbn", isbn.formattedValue))
            .andExpect(status().isNoContent())

        mockMvc.perform(get("/books/{isbn}", LES_SENS_DU_VOTE.isbn))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.availability.size()").value(1))
            .andExpect(jsonPath("$.availability[0].id").value(BNF.id))
            .andExpect(jsonPath("$.availability[0].name").value(BNF.name))
            .andExpect(jsonPath("$.availability[0].address.line1").value(BNF.address?.line1))
            .andExpect(jsonPath("$.availability[0].address.line2").value(BNF.address?.line2))
            .andExpect(jsonPath("$.availability[0].address.postalCode").value(BNF.address?.postalCode))
            .andExpect(jsonPath("$.availability[0].address.city").value(BNF.address?.city))
    }

}
