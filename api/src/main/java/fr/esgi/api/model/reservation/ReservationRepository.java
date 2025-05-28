package fr.esgi.api.model.reservation;

import fr.esgi.api.model.reservation.place.Place;

import java.time.LocalDate;
import java.util.UUID;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    boolean isExistByPlaceAndDate(Place place, LocalDate bookedFor);

    Reservation findById(UUID id) throws ReservationNotFoundException;
}
