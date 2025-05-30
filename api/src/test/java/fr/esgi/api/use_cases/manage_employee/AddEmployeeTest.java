package fr.esgi.api.use_cases.manage_employee;

import fr.esgi.api.dtos.requests.AddEmployeeRequest;
import fr.esgi.api.dtos.responses.AddEmployeeResponse;
import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeRepository;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import fr.esgi.api.model.reservation.employee.EmployeeService;
import fr.esgi.api.model.reservation.employee.email.Email;
import fr.esgi.api.model.reservation.exceptions.UnauthorizedEmployeeCreationException;
import fr.esgi.api.presentation.exceptions.ApiException;
import fr.esgi.api.use_cases.manage_employees.AddEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddEmployeeTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AddEmployee addEmployee;

    private UUID secretaryId;
    private Employee secretary;

    @BeforeEach
    void setup() {
        secretaryId = UUID.randomUUID();
        secretary = new Employee(
                secretaryId,
                "Smith",
                "Anna",
                EmployeeRole.SECRETARY,
                Collections.emptyList(),
                Email.of("anna.secretary@esgi.fr")
        );
    }

    @Test
    void should_create_employee_when_creator_is_secretary() {
        AddEmployeeRequest request = new AddEmployeeRequest("John", "Doe", "john.doe@esgi.fr");
        Employee toCreate  = Employee.create(request);
        Employee persisted = new Employee(
                UUID.randomUUID(),
                toCreate.getLastName(),
                toCreate.getFirstName(),
                toCreate.getRole(),
                Collections.emptyList(),
                toCreate.getEmail()
        );

        when(employeeRepository.getById(secretaryId)).thenReturn(secretary);
        when(employeeService.createEmployee(secretary, request)).thenReturn(toCreate);
        when(employeeRepository.save(toCreate)).thenReturn(persisted);

        AddEmployeeResponse response = addEmployee.execute(secretaryId, request);

        assertNotNull(response);
        assertEquals(persisted.getId(),      response.employeeId());
        assertEquals("John",                 response.firstName());
        assertEquals("Doe",                  response.lastName());
        assertEquals("john.doe@esgi.fr",     response.email());

        verify(employeeRepository).getById(secretaryId);
        verify(employeeService).createEmployee(secretary, request);
        verify(employeeRepository).save(toCreate);
    }

    @Test
    void should_throw_when_creator_is_not_secretary() {
        UUID nonSecretaryId = UUID.randomUUID();
        Employee employee = new Employee(
                nonSecretaryId,
                "Normal",
                "User",
                EmployeeRole.EMPLOYEE,
                Collections.emptyList(),
                Email.of("normal.user@esgi.fr")
        );
        AddEmployeeRequest request = new AddEmployeeRequest("John", "Doe", "john.doe@esgi.fr");

        when(employeeRepository.getById(nonSecretaryId)).thenReturn(employee);
        when(employeeService.createEmployee(employee, request))
                .thenThrow(new UnauthorizedEmployeeCreationException());

        assertThrows(ApiException.class, () -> addEmployee.execute(nonSecretaryId, request));

        verify(employeeRepository).getById(nonSecretaryId);
        verify(employeeService).createEmployee(employee, request);
        verify(employeeRepository, never()).save(any());
    }
}