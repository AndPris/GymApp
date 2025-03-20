package example.controllers;

import example.exceptions.*;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ValidationException.class, TrainerNotFoundException.class, TraineeNotFoundException.class,
            TrainingTypeNotFoundException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleInvalidRequests(RuntimeException ex, WebRequest webRequest) {
        String body = ex.getMessage();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, webRequest);
    }

    @ExceptionHandler(value = {IPAddressBlockedException.class})
    protected ResponseEntity<Object> handleIPAddressBlock(RuntimeException ex, WebRequest webRequest) {
        String body = ex.getMessage();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, webRequest);
    }

    @ExceptionHandler(value = {AuthorizationException.class})
    protected ResponseEntity<Object> handleAuthorizationFail(RuntimeException ex, WebRequest webRequest) {
        String body = ex.getMessage();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.FORBIDDEN, webRequest);
    }
}
