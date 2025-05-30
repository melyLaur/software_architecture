package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.model.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CancelReservation {
    private final ReservationRepository reservationRepository;

    public CancelReservation(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void process(UUID reservationId) {
        reservationRepository.delete(reservationId);
    }
}
