package example.controllers;

import example.exceptions.TraineeNotFoundException;
import example.exceptions.TrainerNotFoundException;
import example.exceptions.TrainingTypeNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RestResponseEntityExceptionHandlerTests {

    private RestResponseEntityExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new RestResponseEntityExceptionHandler();
        webRequest = mock(ServletWebRequest.class);
    }

    @ParameterizedTest
    @MethodSource("exceptionsArgs")
    void handleExceptions(RuntimeException ex) {
        ResponseEntity<Object> response = exceptionHandler.handleInvalidRequests(ex, webRequest);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(ex.getMessage(), response.getBody());
    }

    private static Stream<Arguments> exceptionsArgs() {
        return Stream.of(
                Arguments.of(new ValidationException("Validation failed")),
                Arguments.of(new TrainerNotFoundException("Trainer not found")),
                Arguments.of(new TraineeNotFoundException("Trainee not found")),
                Arguments.of(new TrainingTypeNotFoundException("Training type not found")),
                Arguments.of(new IllegalArgumentException("Invalid argument"))
        );
    }
}