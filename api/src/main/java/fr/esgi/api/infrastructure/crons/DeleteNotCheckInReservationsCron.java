package fr.esgi.api.infrastructure.crons;

import fr.esgi.api.use_cases.reservation.DeleteNotCheckInReservations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeleteNotCheckInReservationsCron {
    private final DeleteNotCheckInReservations deleteNotCheckInReservations;

    public DeleteNotCheckInReservationsCron(DeleteNotCheckInReservations deleteNotCheckInReservations) {
        this.deleteNotCheckInReservations = deleteNotCheckInReservations;
    }

    @Scheduled(cron = "0 0 11 * * *")
    public void process() {
        deleteNotCheckInReservations.process();
    }
}
