package fr.esgi.api.presentation;

import fr.esgi.api.dtos.requests.CreateReservationRequest;
import fr.esgi.api.dtos.responses.GetReservationResponse;
import fr.esgi.api.use_cases.reservation.MakeReservation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class MakeReservationController {
    private final MakeReservation makeReservation;

    public MakeReservationController(MakeReservation makeReservation) {
        this.makeReservation = makeReservation;
    }

    @PostMapping(value = "employees/{id}/reservation")
    public GetReservationResponse submit(@PathVariable UUID id, @Valid @RequestBody CreateReservationRequest createReservationRequest) {
        return this.makeReservation.process(id, createReservationRequest);
    }
}
