package example.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CustomValidator {
    private final jakarta.validation.Validator validator;

    public CustomValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public <T> void validate(T obj) {
        for (ConstraintViolation<T> violation : validator.validate(obj)) {
            throw new ValidationException("Validation error: " + violation.getMessage());
        }
    }
}
