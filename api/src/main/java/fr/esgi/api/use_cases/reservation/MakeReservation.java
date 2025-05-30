package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.dtos.requests.CreateReservationEmployeeRequest;
import fr.esgi.api.dtos.responses.GetReservationResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.reservation.ReservationRepository;
import fr.esgi.api.model.reservation.ReservationService;
import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeRepository;
import fr.esgi.api.model.reservation.place.Place;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class MakeReservation {
    private final ReservationService reservationService;
    private final EmployeeRepository employeeRepository;
    private final ReservationRepository reservationRepository;

    public MakeReservation(ReservationService reservationService, EmployeeRepository employeeRepository, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.employeeRepository = employeeRepository;
        this.reservationRepository = reservationRepository;
    }

    public GetReservationResponse processForEmployee(UUID employeeId, CreateReservationEmployeeRequest createReservationEmployeeRequest) {
        boolean electricalPlaceNeeded = createReservationEmployeeRequest.electricalPlaceNeeded();
        LocalDate bookedFor = createReservationEmployeeRequest.bookedFor();
        try {
            Employee employee = this.employeeRepository.getById(employeeId);
            Place place = this.reservationService.findAvailablePlaceForEmployee(employee, electricalPlaceNeeded, bookedFor);
            Reservation reservation = Reservation.create(employee, place, bookedFor);
            Reservation reservationSaved = this.reservationRepository.save(reservation);

            return new GetReservationResponse(
                    reservationSaved.getId(),
                    reservation.getBookedFor(),
                    reservation.getPlace().getIdentifier().toString(),
                    electricalPlaceNeeded
            );

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
