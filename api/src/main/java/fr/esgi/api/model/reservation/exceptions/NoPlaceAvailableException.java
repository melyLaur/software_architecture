package fr.esgi.api.model.reservation.exceptions;

import fr.esgi.api.model.DomainException;

public class NoPlaceAvailableException extends DomainException {
    private static final String MESSAGE = "No place available.";

    public NoPlaceAvailableException() {
        super(MESSAGE);
    }
}
