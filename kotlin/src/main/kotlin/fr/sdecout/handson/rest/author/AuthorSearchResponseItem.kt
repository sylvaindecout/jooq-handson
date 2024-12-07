package fr.sdecout.handson.rest.author

import com.fasterxml.jackson.annotation.JsonUnwrapped
import fr.sdecout.handson.rest.shared.AuthorField
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class AuthorSearchResponseItem(
    @field:JsonUnwrapped @field:NotNull @field:Valid val author: AuthorField?,
)
