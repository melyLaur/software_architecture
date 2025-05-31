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

/**
 * REST controller for managing reservations (employees and managers).
 */
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

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id the UUID of the reservation
     * @return reservation details
     */
    @GetMapping(value = "/reservations/{id}")
    public GetReservationResponse getById(@PathVariable UUID id) {
        return this.getReservation.process(id);
    }

    /**
     * Retrieves all upcoming reservations for a given employee.
     *
     * @param id the UUID of the employee
     * @return list of reservation responses
     */
    @GetMapping(value = "employees/{id}/reservations")
    public List<GetReservationResponse> getAll(@PathVariable UUID id) {
        return this.employeeReservations.getAll(id);
    }

    /**
     * Creates a reservation for a regular employee.
     *
     * @param id the UUID of the employee
     * @param createReservationEmployeeRequest reservation creation details
     * @return created reservation response
     */
    @PostMapping(value = "employees/{id}/reservations")
    public GetReservationResponse processForEmployee(@PathVariable UUID id, @Valid @RequestBody CreateReservationEmployeeRequest createReservationEmployeeRequest) {
        return this.makeReservation.processForEmployee(id, createReservationEmployeeRequest);
    }

    /**
     * Creates multiple reservations for a manager over a date range.
     *
     * @param id the UUID of the manager
     * @param createReservationManagerRequest manager reservation request with number of days
     * @return list of created reservation responses
     */
    @PostMapping(value = "managers/{id}/reservations")
    public List<GetReservationResponse> processForManager(@PathVariable UUID id, @Valid @RequestBody CreateReservationManagerRequest createReservationManagerRequest) {
        return this.makeReservation.processForManager(id, createReservationManagerRequest);
    }

    /**
     * Cancels a reservation by its ID.
     *
     * @param id the UUID of the reservation to delete
     */
    @DeleteMapping(value = "reservations/{id}")
    public void deleteReservation(@PathVariable UUID id) {
        this.cancelReservation.process(id);
    }
}
