package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.dtos.responses.GetReservationResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.reservation.place.PlaceType;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Use case for retrieving all upcoming reservations of an employee.
 */
@Service
public class GetEmployeeReservations {
    private final EmployeeRepository employeeRepository;

    public GetEmployeeReservations(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    /**
     * Retrieve all reservations of an employee that are not in the past and not yet checked in.
     *
     * @param employeeId the UUID of the employee
     * @return a list of GetReservationResponse DTOs, sorted by date
     * @throws ApiException with 404 status if employee not found
     */
    public List<GetReservationResponse> getAll(UUID employeeId) {
        try {
            LocalDate now = LocalDate.now();

            Employee employee = employeeRepository.getById(employeeId);
            List<Reservation> reservations = employee
                    .getReservations()
                    .stream()
                    .filter(reservation -> !reservation.getBookedFor().isBefore(now) && !reservation.isCheckedIn())
                    .sorted(Comparator.comparing(Reservation::getBookedFor))
                    .toList();
            return reservations.stream().map(this::mapToGetReservationResponse).toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private GetReservationResponse mapToGetReservationResponse(Reservation reservation) {
        boolean isElectric = reservation.getPlace().getType() == PlaceType.ELECTRICAL;

        return new GetReservationResponse(
                reservation.getId(),
                reservation.getBookedFor(),
                reservation.getPlace().getIdentifier().toString(),
                isElectric
        );
    }
}
