package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.dtos.responses.GetReservationResponse;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeNotFoundException;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.model.employee.EmployeeRole;
import fr.esgi.api.model.employee.email.Email;
import fr.esgi.api.model.reservation.place.Place;
import fr.esgi.api.model.reservation.place.PlaceIdentifier;
import fr.esgi.api.model.reservation.place.PlaceStatus;
import fr.esgi.api.model.reservation.place.PlaceType;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEmployeeReservationsTest {
    protected LocalDate mockNow;
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private GetEmployeeReservations getEmployeeReservations;

    @BeforeEach
    void setUp() {
        mockNow = LocalDate.of(2025, 6, 2);
    }

    @Test
    void should_get_all() {
        Place p1 = new Place(UUID.randomUUID(), PlaceIdentifier.of('B', (short) 3), PlaceType.NORMAL, PlaceStatus.AVAILABLE);
        Place p2 = new Place(UUID.randomUUID(), PlaceIdentifier.of('C', (short) 7), PlaceType.ELECTRICAL, PlaceStatus.AVAILABLE);

        LocalDate bookedFor = mockNow.plusDays(3);

        Reservation r1 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(1), false);
        Reservation r2 = new Reservation(UUID.randomUUID(), p2, bookedFor.plusDays(8), false);
        Reservation r3 = new Reservation(UUID.randomUUID(), p1, bookedFor.plusDays(3), false);
        List<Reservation> reservations = List.of(r1, r2, r3);

        Employee employee = new Employee(UUID.randomUUID(), "Doe", "John", EmployeeRole.EMPLOYEE, reservations, Email.of("employee.test@gmail.com"));

        List<GetReservationResponse> expected = List.of(
                new GetReservationResponse(r1.getId(), r1.getBookedFor(), r1.getPlace().getIdentifier().toString(), false),
                new GetReservationResponse(r2.getId(), r2.getBookedFor(), r2.getPlace().getIdentifier().toString(), true),
                new GetReservationResponse(r3.getId(), r3.getBookedFor(), r3.getPlace().getIdentifier().toString(), false)
        );

        when(employeeRepository.getById(employee.getId())).thenReturn(employee);

        List<GetReservationResponse> results = getEmployeeReservations.getAll(employee.getId());

        assertEquals(expected, results);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void should_throw_an_exception_if_employee_not_found() {
        UUID id = UUID.randomUUID();
        doThrow(EmployeeNotFoundException.class).when(employeeRepository).getById(id);
        assertThrows(ApiException.class, () -> getEmployeeReservations.getAll(id));
    }

}