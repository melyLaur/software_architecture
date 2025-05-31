package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.dtos.requests.CreateReservationEmployeeRequest;
import fr.esgi.api.dtos.requests.CreateReservationManagerRequest;
import fr.esgi.api.dtos.responses.GetReservationResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.reservation.ReservationMailService;
import fr.esgi.api.model.reservation.ReservationRepository;
import fr.esgi.api.model.reservation.ReservationService;
import fr.esgi.api.model.reservation.place.Place;
import fr.esgi.api.model.reservation.place.PlaceType;
import fr.esgi.api.presentation.exceptions.ApiException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Use case for handling reservations, both for employees and managers.
 */
@Service
public class MakeReservation {
    private final ReservationService reservationService;
    private final EmployeeRepository employeeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMailService reservationMailService;

    public MakeReservation(ReservationService reservationService, EmployeeRepository employeeRepository, ReservationRepository reservationRepository, ReservationMailService reservationMailService) {
        this.reservationService = reservationService;
        this.employeeRepository = employeeRepository;
        this.reservationRepository = reservationRepository;
        this.reservationMailService = reservationMailService;
    }

    /**
     * Process a reservation request for a standard employee.
     *
     * @param employeeId the UUID of the employee
     * @param createReservationEmployeeRequest the reservation details (date, type)
     * @return the created reservation response
     * @throws ApiException if the employee is not found or the reservation is invalid
     */
    public GetReservationResponse processForEmployee(UUID employeeId, CreateReservationEmployeeRequest createReservationEmployeeRequest) {
        boolean electricalPlaceNeeded = createReservationEmployeeRequest.electricalPlaceNeeded();
        LocalDate bookedFor = createReservationEmployeeRequest.bookedFor();
        try {
            Employee employee = this.employeeRepository.getById(employeeId);
            Place place = this.reservationService.findAvailablePlaceForEmployee(employee, electricalPlaceNeeded, bookedFor);
            Reservation reservation = Reservation.create(employee, place, bookedFor);
            Reservation reservationSaved = this.reservationRepository.save(reservation);

            this.reservationMailService.sendRecap(employee, reservationSaved, electricalPlaceNeeded);

            return mapToGetReservationResponse(reservationSaved);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Process a reservation request for a manager across multiple days.
     *
     * @param employeeId the UUID of the manager
     * @param createReservationManagerRequest the reservation request including start date and number of days
     * @return list of created reservations
     * @throws ApiException if constraints are violated
     */
    public List<GetReservationResponse> processForManager(UUID employeeId, @Valid CreateReservationManagerRequest createReservationManagerRequest) {
        boolean electricalPlaceNeeded = createReservationManagerRequest.electricalPlaceNeeded();
        LocalDate bookedFor = createReservationManagerRequest.bookedFor();
        Integer numberDays = createReservationManagerRequest.daysNumber();

        try {

            Employee employee = this.employeeRepository.getById(employeeId);
            Place place = this.reservationService.findAvailablePlaceForManager(employee, electricalPlaceNeeded, bookedFor, numberDays);

            List<Reservation> reservations = new ArrayList<>();
            for (int i = 0; i < numberDays; i++) {
                reservations.add(Reservation.create(employee, place, bookedFor.plusDays(i)));
            }

            List<Reservation> reservationsSaved = this.reservationRepository.saveAll(reservations);

            return reservationsSaved.stream().map(this::mapToGetReservationResponse).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
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
