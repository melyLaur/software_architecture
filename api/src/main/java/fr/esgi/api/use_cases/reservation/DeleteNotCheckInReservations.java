package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.model.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

/**
 * Use case for deleting reservations where the user did not check in.
 */
@Service
public class DeleteNotCheckInReservations {
    private final ReservationRepository reservationRepository;

    public DeleteNotCheckInReservations(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Executes the deletion of all reservations that were not checked in.
     */
    public void process() {
        reservationRepository.deleteNotCheckInReservation();
    }
}
