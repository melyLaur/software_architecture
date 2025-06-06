package fr.esgi.api.presentation.exceptions;

import org.springframework.http.HttpStatus;

public class UnknownApiException extends ApiException {
    private static final String MESSAGE = "Unknown error";
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public UnknownApiException(String cause) {
        super(STATUS, MESSAGE + cause);
    }
}
