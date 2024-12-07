package fr.sdecout.handson.rest.library

import com.fasterxml.jackson.annotation.JsonUnwrapped
import fr.sdecout.handson.rest.shared.LibraryField
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class LibrarySearchResponseItem(
    @field:JsonUnwrapped @field:NotNull @field:Valid val library: LibraryField?,
)
