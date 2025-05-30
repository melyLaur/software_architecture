package fr.esgi.api.model.reservation;

import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeRole;
import fr.esgi.api.model.employee.email.Email;
import fr.esgi.api.model.reservation.exceptions.CannotBookException;
import fr.esgi.api.model.reservation.exceptions.CannotBookExceptionMessage;
import fr.esgi.api.model.reservation.exceptions.NoPlaceAvailableException;
import fr.esgi.api.model.reservation.exceptions.UnsupportedEmployeeRoleException;
import fr.esgi.api.model.reservation.place.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    protected LocalDate mockNow;

    @BeforeEach
    void setUp() {
        mockNow = LocalDate.of(2025, 6, 2);
    }

    @Test
    void should_return_first_available_place_when_multiple_places_are_available() {
        Place p1 = new Place(UUID.randomUUID(), PlaceIdentifier.of('B', (short) 3), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        Place p2 = new Place(UUID.randomUUID(), PlaceIdentifier.of('C', (short) 7), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        List<Place> places = List.of(p1, p2);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of(), Email.of("employee.test@gmail.com"));
        boolean electricalPlaceNeeded = false;

        LocalDate bookedFor = mockNow.plusDays(3);

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

        LocalDate bookedFor = mockNow.plusDays(3);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of(), Email.of("employee.test@gmail.com"));
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

        LocalDate bookedFor = mockNow.plusDays(3);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of(), Email.of("employee.test@gmail.com"));
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

        LocalDate bookedFor = mockNow.plusDays(3);

        Reservation r1 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(1), bookedFor.plusDays(1), false);
        Reservation r2 = new Reservation(UUID.randomUUID(), p2, bookedFor.plusDays(8), bookedFor.plusDays(2), false);
        Reservation r3 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(3), bookedFor.plusDays(3), false);
        Reservation r4 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(4), bookedFor.plusDays(4), false);
        Reservation r5 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(10), bookedFor.plusDays(10), false);
        List<Reservation> reservations = List.of(r1, r2, r3, r4, r5);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, reservations, Email.of("employee.test@gmail.com"));

        CannotBookException e = assertThrows(CannotBookException.class, () -> reservationService.findAvailablePlaceForEmployee(employee, true, bookedFor));
        assertEquals(e.getMessage(), CannotBookExceptionMessage.MAXIMUM_POSSIBLE_RESERVATION_DAYS_EXCEED.getMessage());
    }

    @Test
    void should_throw_exception_when_employee_already_has_reservation_for_given_day() {
        Place p1 = new Place(UUID.randomUUID(), PlaceIdentifier.of('B', (short) 3), PlaceType.NORMAL, PlaceStatus.AVAILABLE);

        LocalDate bookedFor = mockNow.plusDays(3);

        Reservation r1 = new Reservation(UUID.randomUUID(), p1, bookedFor, bookedFor, false);
        List<Reservation> reservations = List.of(r1);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, reservations, Email.of("employee.test@gmail.com"));

        CannotBookException e = assertThrows(CannotBookException.class, () -> reservationService.findAvailablePlaceForEmployee(employee, true, bookedFor));
        assertEquals(e.getMessage(), CannotBookExceptionMessage.RESERVATION_ALREADY_EXITS.getMessage());
    }

    @Test
    void should_throw_exception_when_booking_date_is_in_the_past() {
        LocalDate now = LocalDate.now();
        LocalDate bookedFor = now.minusDays(3);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of(), Email.of("employee.test@gmail.com"));

        CannotBookException e = assertThrows(CannotBookException.class, () -> reservationService.findAvailablePlaceForEmployee(employee, true, bookedFor));
        assertEquals(e.getMessage(), CannotBookExceptionMessage.INVALID_DATE.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBookingOnWeekend() {
        LocalDate saturday = LocalDate.of(2025, 5, 31);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, List.of(), Email.of("employee.test@gmail.com"));
        CannotBookException e = assertThrows(CannotBookException.class, () -> reservationService.findAvailablePlaceForEmployee(employee, true, saturday));
        assertEquals(CannotBookExceptionMessage.INVALID_WORKING_DAYS.getMessage(), e.getMessage());
    }

    @Test
    void should_accept_reservation_on_monday() {
        LocalDate monday = LocalDate.of(2025, 6, 2);
        Place place = new Place(UUID.randomUUID(), PlaceIdentifier.of('A', (short)1), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        Employee employee = new Employee(UUID.randomUUID(), "Valid", "Employee", EmployeeRole.EMPLOYEE, List.of(), Email.of("valid@employee.com"));

        when(placeRepository.getAvailablePlaces(PlaceType.NORMAL)).thenReturn(List.of(place));
        when(reservationRepository.isExistByPlaceAndDate(place, monday)).thenReturn(false);

        Place result = reservationService.findAvailablePlaceForEmployee(employee, false, monday);

        assertEquals(place, result);
    }

    @Test
    void should_return_place_when_available_for_30_consecutive_days() {
        Place place = new Place(UUID.randomUUID(), PlaceIdentifier.of('A', (short)1), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        Employee manager = new Employee(UUID.randomUUID(), "Manager", "Alice", EmployeeRole.MANAGER, List.of(), Email.of("manager@test.com"));

        LocalDate startDate = mockNow.plusDays(1);
        when(placeRepository.getAvailablePlaces(PlaceType.NORMAL)).thenReturn(List.of(place));
        for (int i = 0; i < 30; i++) {
            when(reservationRepository.isExistByPlaceAndDate(place, startDate.plusDays(i))).thenReturn(false);
        }

        Place result = reservationService.findAvailablePlaceForEmployee(manager, false, startDate);

        assertEquals(place, result);
    }

    @Test
    void should_throw_exception_when_place_not_available_all_30_days() {
        Place place = new Place(UUID.randomUUID(), PlaceIdentifier.of('A', (short)1), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        Employee manager = new Employee(UUID.randomUUID(), "Manager", "Alice", EmployeeRole.MANAGER, List.of(), Email.of("manager@test.com"));

        LocalDate startDate = mockNow.plusDays(1);
        when(placeRepository.getAvailablePlaces(PlaceType.NORMAL)).thenReturn(List.of(place));
        for (int i = 0; i <= 12; i++) {
            when(reservationRepository.isExistByPlaceAndDate(place, startDate.plusDays(i)))
                    .thenReturn(i == 12);
        }

        assertThrows(NoPlaceAvailableException.class, () -> reservationService.findAvailablePlaceForEmployee(manager, false, startDate));
    }

    @Test
    void should_call_repository_with_electrical_place_type_for_manager() {
        Place electricalPlace = new Place(UUID.randomUUID(), PlaceIdentifier.of('E', (short)1), PlaceType.ELECTRICAL, PlaceStatus.AVAILABLE);
        Employee manager = new Employee(UUID.randomUUID(), "Manager", "Electric", EmployeeRole.MANAGER, List.of(), Email.of("electric@manager.com"));

        LocalDate startDate = mockNow.plusDays(1);

        when(placeRepository.getAvailablePlaces(PlaceType.ELECTRICAL)).thenReturn(List.of(electricalPlace));
        for (int i = 0; i < 30; i++) {
            when(reservationRepository.isExistByPlaceAndDate(electricalPlace, startDate.plusDays(i))).thenReturn(false);
        }

        Place result = reservationService.findAvailablePlaceForEmployee(manager, true, startDate);

        assertEquals(electricalPlace, result);
        verify(placeRepository).getAvailablePlaces(PlaceType.ELECTRICAL);
    }

    @Test
    void should_throw_exception_when_manager_already_has_reservations() {
        LocalDate bookedFor = mockNow.plusDays(3);
        Reservation existingReservation = new Reservation(UUID.randomUUID(), new Place(UUID.randomUUID(), PlaceIdentifier.of('A', (short) 1), PlaceType.NORMAL, PlaceStatus.AVAILABLE), bookedFor.minusDays(1), bookedFor.minusDays(1), false);
        Employee manager = new Employee(UUID.randomUUID(), "Smith", "Anna", EmployeeRole.MANAGER, List.of(existingReservation), Email.of("manager.test@gmail.com"));

        CannotBookException e = assertThrows(CannotBookException.class, () -> reservationService.findAvailablePlaceForEmployee(manager, false, bookedFor));
        assertEquals(CannotBookExceptionMessage.MAXIMUM_POSSIBLE_RESERVATION_DAYS_EXCEED.getMessage(), e.getMessage());
    }

    @Test
    void should_throw_exception_when_role_is_not_supported() {
        Employee invalidRoleEmployee = new Employee(UUID.randomUUID(), "Mystery", "Role", null, List.of(), Email.of("unknown@esgi.fr"));

        LocalDate bookedFor = mockNow.plusDays(2);
        UnsupportedEmployeeRoleException e = assertThrows(UnsupportedEmployeeRoleException.class, () -> reservationService.findAvailablePlaceForEmployee(invalidRoleEmployee, false, bookedFor));
        assertTrue(e.getMessage().contains("Unsupported employee role"));
    }
}