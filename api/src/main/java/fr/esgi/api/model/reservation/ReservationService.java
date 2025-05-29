package fr.esgi.api.model.reservation;

import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import fr.esgi.api.model.reservation.exceptions.CannotBookException;
import fr.esgi.api.model.reservation.exceptions.CannotBookExceptionMessage;
import fr.esgi.api.model.reservation.exceptions.NoPlaceAvailableException;
import fr.esgi.api.model.reservation.place.Place;
import fr.esgi.api.model.reservation.place.PlaceRepository;
import fr.esgi.api.model.reservation.place.PlaceType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private static final int EMPLOYEE_MAXIMUM_POSSIBLE_RESERVATION_DAYS = 5;

    private final PlaceRepository placeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(PlaceRepository placeRepository, ReservationRepository reservationRepository) {
        this.placeRepository = placeRepository;
        this.reservationRepository = reservationRepository;
    }

    public Place findAvailablePlaceForEmployee(Employee employee, boolean electricalPlaceNeeded, LocalDate bookedFor) {
        List<Place> places = new ArrayList<>();

        if (employee.getRole() == EmployeeRole.EMPLOYEE) {
            validateEmployeeReservationConstraints(employee, bookedFor);
            places = findAvailablePlaces(electricalPlaceNeeded, bookedFor);
        }

        if (places.isEmpty()) {
            throw new NoPlaceAvailableException();
        }

        return places.getFirst();
    }

    private void validateEmployeeReservationConstraints(Employee employee, LocalDate bookedFor) {
        LocalDate now = LocalDate.now();
        if (bookedFor.isBefore(now)) {
            throw new CannotBookException(CannotBookExceptionMessage.INVALID_DATE);
        }

        if (!isWorkingDay(bookedFor)) {
            throw new CannotBookException(CannotBookExceptionMessage.INVALID_WORKING_DAYS);
        }

        if (employee.getReservations().size() == EMPLOYEE_MAXIMUM_POSSIBLE_RESERVATION_DAYS) {
            throw new CannotBookException(CannotBookExceptionMessage.EMPLOYEE_MAXIMUM_POSSIBLE_RESERVATION_DAYS_EXCEED);
        }

        for (Reservation reservation : employee.getReservations()) {
            if (reservation.getStartDate().isEqual(bookedFor)) {
                throw new CannotBookException(CannotBookExceptionMessage.RESERVATION_ALREADY_EXITS);
            }
        }
    }

    private List<Place> findAvailablePlaces(boolean electricalPlaceNeeded, LocalDate bookedFor) {
        List<Place> availablePlacesForGivenDate = new ArrayList<>();
        PlaceType placeType = electricalPlaceNeeded ? PlaceType.ELECTRICAL : PlaceType.NORMAL;
        List<Place> availablePlaces = this.placeRepository.getAvailablePlaces(placeType);


        for (Place place : availablePlaces) {
            boolean isBooked = this.reservationRepository.isExistByPlaceAndDate(place, bookedFor);
            if (!isBooked) {
                availablePlacesForGivenDate.add(place);
            }
        }

        return availablePlacesForGivenDate;
    }

    private boolean isWorkingDay(LocalDate date) {
        // todo ABBBBBBBBDDDDDDD
        return true;
    }
}
