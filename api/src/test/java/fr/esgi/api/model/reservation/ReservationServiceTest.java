package fr.esgi.api.model.reservation;

import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import fr.esgi.api.model.reservation.exceptions.CannotBookException;
import fr.esgi.api.model.reservation.exceptions.CannotBookExceptionMessage;
import fr.esgi.api.model.reservation.exceptions.NoPlaceAvailableException;
import fr.esgi.api.model.reservation.place.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void should_return_first_available_place_when_multiple_places_are_available() {
        Place p1 = new Place(UUID.randomUUID(), PlaceIdentifier.of('B', (short) 3), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        Place p2 = new Place(UUID.randomUUID(), PlaceIdentifier.of('C', (short) 7), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        List<Place> places = List.of(p1, p2);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of());
        boolean electricalPlaceNeeded = false;

        LocalDate now = LocalDate.now();
        LocalDate bookedFor = now.plusDays(3);

        when(placeRepository.getAvailablePlaces(PlaceType.NORMAL)).thenReturn(places);
        when(reservationRepository.isExistByPlaceAndDate(p1, bookedFor)).thenReturn(false);
        when(reservationRepository.isExistByPlaceAndDate(p2, bookedFor)).thenReturn(false);

        Place result = reservationService.findAvailablePlaceForEmployee(employee, electricalPlaceNeeded, bookedFor);
        assertEquals(p1, result);
        verifyNoMoreInteractions(placeRepository, reservationRepository);
    }

    @Test
    void should_return_second_place_if_first_is_already_reserved_and_electrical_place_needed() {
        Place p1 = new Place(UUID.randomUUID(), PlaceIdentifier.of('B', (short) 3), PlaceType.ELECTRICAL, PlaceStatus.AVAILABLE);
        Place p2 = new Place(UUID.randomUUID(), PlaceIdentifier.of('C', (short) 7), PlaceType.ELECTRICAL, PlaceStatus.AVAILABLE);
        List<Place> places = List.of(p1, p2);

        LocalDate now = LocalDate.now();
        LocalDate bookedFor = now.plusDays(3);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of());
        boolean electricalPlaceNeeded = true;

        when(placeRepository.getAvailablePlaces(PlaceType.ELECTRICAL)).thenReturn(places);
        when(reservationRepository.isExistByPlaceAndDate(p1, bookedFor)).thenReturn(true);
        when(reservationRepository.isExistByPlaceAndDate(p2, bookedFor)).thenReturn(false);

        Place result = reservationService.findAvailablePlaceForEmployee(employee, electricalPlaceNeeded, bookedFor);
        assertEquals(p2, result);
        verifyNoMoreInteractions(placeRepository, reservationRepository);
    }

    @Test
    void should_throw_exception_when_no_places_are_available() {
        Place p1 = new Place(UUID.randomUUID(), PlaceIdentifier.of('B', (short) 3), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        Place p2 = new Place(UUID.randomUUID(), PlaceIdentifier.of('C', (short) 7), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        List<Place> places = List.of(p1, p2);

        LocalDate now = LocalDate.now();
        LocalDate bookedFor = now.plusDays(3);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of());
        boolean electricalPlaceNeeded = false;

        when(placeRepository.getAvailablePlaces(PlaceType.NORMAL)).thenReturn(places);
        when(reservationRepository.isExistByPlaceAndDate(p1, bookedFor)).thenReturn(true);
        when(reservationRepository.isExistByPlaceAndDate(p2, bookedFor)).thenReturn(true);

        assertThrows(NoPlaceAvailableException.class, () -> reservationService.findAvailablePlaceForEmployee(employee, electricalPlaceNeeded, bookedFor));
    }

    @Test
    void should_throw_exception_when_employee_has_maximum_reservations() {
        Place p1 = new Place(UUID.randomUUID(), PlaceIdentifier.of('B', (short) 3), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        Place p2 = new Place(UUID.randomUUID(), PlaceIdentifier.of('C', (short) 7), PlaceType.NORMAL, PlaceStatus.AVAILABLE);

        LocalDate now = LocalDate.now();
        LocalDate bookedFor = now.plusDays(3);


        Reservation r1 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(1), bookedFor.plusDays(1), false);
        Reservation r2 = new Reservation(UUID.randomUUID(), p2, bookedFor.plusDays(8), bookedFor.plusDays(2), false);
        Reservation r3 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(3), bookedFor.plusDays(3), false);
        Reservation r4 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(4), bookedFor.plusDays(4), false);
        Reservation r5 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(10), bookedFor.plusDays(10), false);
        List<Reservation> reservations = List.of(r1, r2, r3, r4, r5);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, reservations);

        CannotBookException e = assertThrows(CannotBookException.class, () -> reservationService.findAvailablePlaceForEmployee(employee, true, bookedFor));
        assertEquals(e.getMessage(), CannotBookExceptionMessage.EMPLOYEE_MAXIMUM_POSSIBLE_RESERVATION_DAYS_EXCEED.getMessage());
    }

    @Test
    void should_throw_exception_when_employee_already_has_reservation_for_given_day() {
        Place p1 = new Place(UUID.randomUUID(), PlaceIdentifier.of('B', (short) 3), PlaceType.NORMAL, PlaceStatus.AVAILABLE);

        LocalDate now = LocalDate.now();
        LocalDate bookedFor = now.plusDays(3);

        Reservation r1 = new Reservation(UUID.randomUUID(), p1, bookedFor, bookedFor, false);
        List<Reservation> reservations = List.of(r1);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, reservations);

        CannotBookException e = assertThrows(CannotBookException.class, () -> reservationService.findAvailablePlaceForEmployee(employee, true, bookedFor));
        assertEquals(e.getMessage(), CannotBookExceptionMessage.RESERVATION_ALREADY_EXITS.getMessage());
    }

    @Test
    void should_throw_exception_when_booking_date_is_in_the_past() {
        LocalDate now = LocalDate.now();
        LocalDate bookedFor = now.minusDays(3);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of());

        CannotBookException e = assertThrows(CannotBookException.class, () -> reservationService.findAvailablePlaceForEmployee(employee, true, bookedFor));
        assertEquals(e.getMessage(), CannotBookExceptionMessage.INVALID_DATE.getMessage());
    }

    //todo do test if it is not a working day
//    @Test
//    void g() {
//        LocalDate now = LocalDate.now();
//        LocalDate bookedFor = now.minusDays(3);
//
//        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of());
//
//        CannotBookException e = assertThrows(CannotBookException.class, () -> reservationService.findAvailablePlaceForEmployee(employee, true, bookedFor));
//        assertEquals(e.getMessage(), CannotBookExceptionMessage.INVALID_DATE.getMessage());
//    }

}