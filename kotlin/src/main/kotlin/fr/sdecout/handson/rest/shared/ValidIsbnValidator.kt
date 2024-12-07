package fr.sdecout.handson.rest.shared;

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValidIsbnValidator : ConstraintValidator<ValidIsbn, String?> {
    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean =
        value == null || value.replace("-", "").matches(Regex("[0-9]{13}"))
}
