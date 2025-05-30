package fr.esgi.api.model.reservation.exceptions;

import fr.esgi.api.model.DomainException;

public class CannotBookException extends DomainException {
    public CannotBookException(CannotBookExceptionMessage cannotBookExceptionMessage) {
        super(cannotBookExceptionMessage.getMessage());
    }

}
