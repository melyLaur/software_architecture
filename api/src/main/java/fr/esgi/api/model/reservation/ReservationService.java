package fr.esgi.api.model.reservation;

import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import fr.esgi.api.model.reservation.exceptions.CannotBookException;
import fr.esgi.api.model.reservation.exceptions.CannotBookExceptionMessage;
import fr.esgi.api.model.reservation.exceptions.NoPlaceAvailableException;
import fr.esgi.api.model.reservation.exceptions.UnsupportedEmployeeRoleException;
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
    private static final int MANAGER_POSSIBLE_RESERVATION_DAYS = 30;

    private final PlaceRepository placeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(PlaceRepository placeRepository, ReservationRepository reservationRepository) {
        this.placeRepository = placeRepository;
        this.reservationRepository = reservationRepository;
    }

    public Place findAvailablePlaceForEmployee(Employee employee, boolean electricalPlaceNeeded, LocalDate bookedFor) {
        if (employee.getRole() == EmployeeRole.EMPLOYEE) {
            validateEmployeeConstraints(employee, bookedFor);
            return findPlaceForOneDay(electricalPlaceNeeded, bookedFor);
        } else if (employee.getRole() == EmployeeRole.MANAGER) {
            validateManagerConstraints(employee, bookedFor);
            return findPlaceForAMonth(electricalPlaceNeeded, bookedFor);
        }

        throw new UnsupportedEmployeeRoleException();
    }

    private void validateEmployeeConstraints(Employee employee, LocalDate bookedFor) {
        validateReservationConstraints(employee, bookedFor);

        if (!isWorkingDay(bookedFor)) {
            throw new CannotBookException(CannotBookExceptionMessage.INVALID_WORKING_DAYS);
        }

        if (employee.getReservations().size() >= EMPLOYEE_MAXIMUM_POSSIBLE_RESERVATION_DAYS) {
            throw new CannotBookException(CannotBookExceptionMessage.MAXIMUM_POSSIBLE_RESERVATION_DAYS_EXCEED);
        }
    }

    private void validateManagerConstraints(Employee employee, LocalDate bookedFor) {
        validateReservationConstraints(employee, bookedFor);

        if (!employee.getReservations().isEmpty()) {
            throw new CannotBookException(CannotBookExceptionMessage.MAXIMUM_POSSIBLE_RESERVATION_DAYS_EXCEED);
        }
    }

    private Place findPlaceForOneDay(boolean electricalPlaceNeeded, LocalDate bookedFor) {
        List<Place> availablePlaces = findAvailablePlaces(electricalPlaceNeeded, bookedFor);

        if (availablePlaces.isEmpty()) {
            throw new NoPlaceAvailableException();
        }

        return availablePlaces.getFirst();
    }

    private Place findPlaceForAMonth(boolean electricalPlaceNeeded, LocalDate bookedFor) {
        PlaceType placeType = electricalPlaceNeeded ? PlaceType.ELECTRICAL : PlaceType.NORMAL;
        List<Place> allPlaces = this.placeRepository.getAvailablePlaces(placeType);

        for(Place place : allPlaces) {
            boolean allDaysAvailable = true;

            for (int i = 0; i < MANAGER_POSSIBLE_RESERVATION_DAYS; i++) {
                LocalDate currentDate = bookedFor.plusDays(i);
                if(this.reservationRepository.isExistByPlaceAndDate(place, currentDate)) {
                    allDaysAvailable = false;
                    break;
                }
            }

            if (allDaysAvailable) {
                return place;
            }
        }

        throw new NoPlaceAvailableException();
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

    private void validateReservationConstraints(Employee employee, LocalDate bookedFor) {
        if (bookedFor.isBefore(LocalDate.now())) {
            throw new CannotBookException(CannotBookExceptionMessage.INVALID_DATE);
        }

        for (Reservation reservation : employee.getReservations()) {
            if (reservation.getStartDate().isEqual(bookedFor)) {
                throw new CannotBookException(CannotBookExceptionMessage.RESERVATION_ALREADY_EXITS);
            }
        }
    }

    private boolean isWorkingDay(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek >= 1 && dayOfWeek <= 5;
    }
}
