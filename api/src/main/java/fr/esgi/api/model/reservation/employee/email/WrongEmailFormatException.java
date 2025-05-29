package fr.esgi.api.model.reservation.employee.email;

import fr.esgi.api.model.DomainException;

public final class WrongEmailFormatException extends DomainException {
    private static final String MESSAGE = "Le format de l'email est invalide";

    public WrongEmailFormatException() {
        super(MESSAGE);
    }
}