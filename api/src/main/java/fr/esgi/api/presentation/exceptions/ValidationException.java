package fr.esgi.api.presentation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends ApiException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private ValidationException(String message) {
        super(STATUS, message);
    }

    public static ValidationException of(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ValidationException(errors.toString());
    }
}