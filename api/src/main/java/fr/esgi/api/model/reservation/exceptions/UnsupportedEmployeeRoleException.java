package fr.esgi.api.model.reservation.exceptions;

import fr.esgi.api.model.DomainException;

public class UnsupportedEmployeeRoleException extends DomainException {
    private static final String MESSAGE = "Unsupported employee role";
    public UnsupportedEmployeeRoleException() {
        super(MESSAGE);
    }
}
