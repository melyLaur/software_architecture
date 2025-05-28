package fr.esgi.api.presentation;

import fr.esgi.api.dtos.responses.ErrorResponse;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 * Catches all exceptions thrown in the application and maps them to a structured error response.
 */
@RestControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * Handles all exceptions globally and converts them into ApiException responses.
     *
     * @param ex the exception thrown
     * @return a ResponseEntity containing an ErrorResponse with status, code, and message
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ApiException exception = ApiException.from(ex);
        HttpStatus status = exception.getStatus();
        ErrorResponse errorResponse = new ErrorResponse(status.name(), exception.getMessage());
        logger.error("Error : message {}", errorResponse.message());
        return ResponseEntity.status(status).body(errorResponse);
    }
}
