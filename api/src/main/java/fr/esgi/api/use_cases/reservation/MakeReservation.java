package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.dtos.requests.CreateReservationRequest;
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

    public GetReservationResponse process(UUID employeeId, CreateReservationRequest createReservationRequest) {
        boolean electricalPlaceNeeded = createReservationRequest.electricalPlaceNeeded();
        LocalDate bookedFor = createReservationRequest.bookedFor();

        try {
            Employee employee = this.employeeRepository.getById(employeeId);
            Place place = this.reservationService.findAvailablePlaceForEmployee(employee, electricalPlaceNeeded, bookedFor);
            Reservation reservation = Reservation.createFromEmployee(employee, place, bookedFor);

            Reservation reservationSaved = this.reservationRepository.save(reservation);
            // todo : send an email
            // todo : historic

            return new GetReservationResponse(
                    reservationSaved.getId(),
                    reservation.getStartDate(),
                    reservation.getPlace().getIdentifier().toString(),
                    electricalPlaceNeeded
            );

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
