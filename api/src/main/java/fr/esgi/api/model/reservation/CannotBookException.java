package fr.esgi.api.model.reservation;

import fr.esgi.api.model.DomainException;

public class CannotBookException extends DomainException {
    public CannotBookException(String message) {
        super(message);
    }
}
