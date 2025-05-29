package fr.esgi.api.model.reservation.place;

import fr.esgi.api.model.DomainException;

public class PlaceNotFoundException extends DomainException {
    private static final String MESSAGE = "La place n'existe pas.";
    public PlaceNotFoundException() {
        super(MESSAGE);
    }
}
