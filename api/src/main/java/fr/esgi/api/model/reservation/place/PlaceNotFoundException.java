package fr.esgi.api.model.reservation.place;

import fr.esgi.api.model.DomainException;

public class PlaceNotFoundException extends DomainException {
    private static final String MESSAGE = "Place not found.";
    public PlaceNotFoundException() {
        super(MESSAGE);
    }
}
