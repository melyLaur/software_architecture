package fr.esgi.api.model.reservation.exceptions;

import fr.esgi.api.model.DomainException;

public class ReservationNotFoundException extends DomainException {
    private static final String MESSAGE = "Reservation not found";
    public ReservationNotFoundException() {
        super(MESSAGE);
    }
}
