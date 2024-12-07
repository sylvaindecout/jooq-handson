package fr.sdecout.handson.rest.shared;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidIsbnValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidIsbn {
    String message() default "Value is not a valid ISBN";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
