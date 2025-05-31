package fr.esgi.api.infrastructure.crons;

import fr.esgi.api.use_cases.reservation.DeleteNotCheckInReservations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled task that deletes all reservations where users did not check in.
 * <p>
 * This cron job runs daily at 11:00 AM and delegates the cleanup logic
 * to the corresponding use case.
 */
@Component
public class DeleteNotCheckInReservationsCron {
    private final DeleteNotCheckInReservations deleteNotCheckInReservations;

    public DeleteNotCheckInReservationsCron(DeleteNotCheckInReservations deleteNotCheckInReservations) {
        this.deleteNotCheckInReservations = deleteNotCheckInReservations;
    }

    /**
     * Executes the scheduled cleanup of unchecked-in reservations.
     * <p>
     * This method is triggered automatically based on the cron expression:
     * - Every day at 11:00 AM server time
     * <p>
     * It delegates the logic to the {@link DeleteNotCheckInReservations} use case.
     */
    @Scheduled(cron = "0 0 11 * * *")
    public void process() {
        deleteNotCheckInReservations.process();
    }
}
