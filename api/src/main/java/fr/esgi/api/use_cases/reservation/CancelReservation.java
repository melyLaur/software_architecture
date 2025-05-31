package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.model.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for canceling a reservation by its unique identifier.
 */
@Service
public class CancelReservation {
    private final ReservationRepository reservationRepository;

    public CancelReservation(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Cancels the reservation associated with the provided ID.
     *
     * @param reservationId the UUID of the reservation to cancel
     */
    public void process(UUID reservationId) {
        reservationRepository.delete(reservationId);
    }
}
