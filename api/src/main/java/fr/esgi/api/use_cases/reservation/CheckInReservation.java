package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.reservation.ReservationRepository;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CheckInReservation {
    private final ReservationRepository reservationRepository;

    public CheckInReservation(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void validate(UUID reservationId) {
        try {
            Reservation reservation = reservationRepository.findById(reservationId);
            reservation.setCheckedIn(true);
            reservationRepository.update(reservation);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }
}
