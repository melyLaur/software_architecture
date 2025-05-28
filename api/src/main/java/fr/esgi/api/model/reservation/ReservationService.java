package fr.esgi.api.model.reservation;

import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
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

    public void validateEmployeeReservationConstraints(Employee employee, LocalDate bookedFor) {
        if (employee.getReservations().size() == EMPLOYEE_MAXIMUM_POSSIBLE_RESERVATION_DAYS) {
            throw new CannotBookException("tu peux pas réserver car déjà max de réservation.");
        }

        for (Reservation reservation : employee.getReservations()) {
            if (reservation.getStartDate().isEqual(bookedFor)) {
                throw new CannotBookException("tu as déjà une réservation pour ce jour.");
            }
        }

        LocalDate now = LocalDate.now();
        if (bookedFor.isBefore(now)) {
            throw new CannotBookException("Impossible de réserver dans le passé. La date doit être aujourd'hui ou ultérieure.");
        }

        if (!isWorkingDay(bookedFor)) {
            throw new CannotBookException("Impossible de réserver car pas jour ouvré.");
        }
    }

    public List<Place> findAvailablePlaces(boolean electricalPlaceNeeded, LocalDate bookedFor) {
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
