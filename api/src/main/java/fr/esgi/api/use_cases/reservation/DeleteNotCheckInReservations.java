package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.model.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteNotCheckInReservations {
    private final ReservationRepository reservationRepository;

    public DeleteNotCheckInReservations(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void process() {
        reservationRepository.deleteNotCheckInReservation();
    }
}
