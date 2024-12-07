package fr.sdecout.handson.rest.shared;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public final class ValidIsbnValidator implements ConstraintValidator<ValidIsbn, String> {

    private static final Pattern PATTERN = Pattern.compile("[0-9]{13}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || PATTERN.matcher(value.replace("-", "")).matches();
    }

}
