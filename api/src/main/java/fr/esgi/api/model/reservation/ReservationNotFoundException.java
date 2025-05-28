package fr.esgi.api.model.reservation;

import fr.esgi.api.model.DomainException;

public class ReservationNotFoundException extends DomainException {
    private static final String MESSAGE = "La réservation n'existe pas.";
    public ReservationNotFoundException() {
        super(MESSAGE);
    }
}
