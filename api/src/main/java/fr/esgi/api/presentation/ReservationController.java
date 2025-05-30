package fr.esgi.api.presentation;

import fr.esgi.api.dtos.requests.CreateReservationEmployeeRequest;
import fr.esgi.api.dtos.requests.CreateReservationManagerRequest;
import fr.esgi.api.dtos.responses.GetReservationResponse;
import fr.esgi.api.use_cases.reservation.CancelReservation;
import fr.esgi.api.use_cases.reservation.GetEmployeeReservations;
import fr.esgi.api.use_cases.reservation.GetReservation;
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
    private final GetReservation getReservation;


    public ReservationController(MakeReservation makeReservation, CancelReservation cancelReservation, GetEmployeeReservations employeeReservations, GetReservation getReservation) {
        this.makeReservation = makeReservation;
        this.cancelReservation = cancelReservation;
        this.employeeReservations = employeeReservations;
        this.getReservation = getReservation;
    }

    @GetMapping(value = "/reservations/{id}")
    public GetReservationResponse getById(@PathVariable UUID id) {
        return this.getReservation.process(id);
    }

    @GetMapping(value = "employees/{id}/reservations")
    public List<GetReservationResponse> getAll(@PathVariable UUID id) {
        return this.employeeReservations.getAll(id);
    }

    @PostMapping(value = "employees/{id}/reservations")
    public GetReservationResponse processForEmployee(@PathVariable UUID id, @Valid @RequestBody CreateReservationEmployeeRequest createReservationEmployeeRequest) {
        return this.makeReservation.processForEmployee(id, createReservationEmployeeRequest);
    }

    @PostMapping(value = "managers/{id}/reservations")
    public List<GetReservationResponse> processForManager(@PathVariable UUID id, @Valid @RequestBody CreateReservationManagerRequest createReservationManagerRequest) {
        return this.makeReservation.processForManager(id, createReservationManagerRequest);
    }

    @DeleteMapping(value = "reservations/{id}")
    public void deleteReservation(@PathVariable UUID id) {
        this.cancelReservation.process(id);
    }
}
