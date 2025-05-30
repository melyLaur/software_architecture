package fr.esgi.api.presentation;

import fr.esgi.api.dtos.requests.CreateReservationRequest;
import fr.esgi.api.dtos.responses.GetReservationResponse;
import fr.esgi.api.use_cases.reservation.CancelReservation;
import fr.esgi.api.use_cases.reservation.GetEmployeeReservations;
import fr.esgi.api.use_cases.reservation.MakeReservation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class ReservationController {
    private final MakeReservation makeReservation;
    private final CancelReservation cancelReservation;
    private final GetEmployeeReservations employeeReservations;


    public ReservationController(MakeReservation makeReservation, CancelReservation cancelReservation, GetEmployeeReservations employeeReservations) {
        this.makeReservation = makeReservation;
        this.cancelReservation = cancelReservation;
        this.employeeReservations = employeeReservations;
    }

    @GetMapping(value = "employees/{id}/reservations")
    public List<GetReservationResponse> getAll(@PathVariable UUID id) {
        return this.employeeReservations.getAll(id);
    }

    @PostMapping(value = "employees/{id}/reservations")
    public GetReservationResponse process(@PathVariable UUID id, @Valid @RequestBody CreateReservationRequest createReservationRequest) {
        return this.makeReservation.process(id, createReservationRequest);
    }

    @DeleteMapping(value = "reservations/{id}")
    public void deleteReservation(@PathVariable UUID id) {
        this.cancelReservation.process(id);
    }
}
