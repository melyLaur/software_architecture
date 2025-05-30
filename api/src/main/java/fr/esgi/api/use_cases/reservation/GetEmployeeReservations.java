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
import java.util.List;
import java.util.UUID;

@Service
public class GetEmployeeReservations {
    private final EmployeeRepository employeeRepository;

    public GetEmployeeReservations(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<GetReservationResponse> getAll(UUID employeeId) {
        try {
            LocalDate now = LocalDate.now();

            Employee employee = employeeRepository.getById(employeeId);
            List<Reservation> reservations = employee
                    .getReservations()
                    .stream()
                    .filter(reservation -> !reservation.getStartDate().isBefore(now))
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
                reservation.getStartDate(),
                reservation.getPlace().getIdentifier().toString(),
                isElectric
        );
    }
}
