package fr.sdecout.handson.rest.shared

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [ValidIsbnValidator::class])
@Target(FIELD, VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class ValidIsbn(
    val message: String = "Value is not a valid ISBN",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
