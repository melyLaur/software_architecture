package fr.esgi.api.presentation;

import fr.esgi.api.use_cases.reservation.CheckInReservation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class CheckInReservationController {
    private final CheckInReservation checkInReservation;

    public CheckInReservationController(CheckInReservation checkInReservation) {
        this.checkInReservation = checkInReservation;
    }

    @PatchMapping(value = "reservations/{id}/check-in")
    public void validate(@PathVariable UUID id) {
        this.checkInReservation.validate(id);
    }
}
