package example.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;

public class Validator {
    private static final jakarta.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T obj) {
        for (ConstraintViolation<T> violation : validator.validate(obj)) {
            throw new ValidationException("Validation error: " + violation.getMessage());
        }
    }
}
