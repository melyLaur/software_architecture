package fr.esgi.api.model.reservation.exceptions;

import fr.esgi.api.model.DomainException;

public class UnauthorizedEmployeeCreationException extends DomainException {
    private static final String MESSAGE = "Employee creation failed";
    public UnauthorizedEmployeeCreationException() {
        super(MESSAGE);
    }
}
