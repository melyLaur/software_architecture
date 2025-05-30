package fr.esgi.api.use_cases.manage_employee;

import fr.esgi.api.dtos.requests.AddEmployeeRequest;
import fr.esgi.api.dtos.responses.AddEmployeeResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.model.employee.EmployeeRole;
import fr.esgi.api.presentation.exceptions.ApiException;
import fr.esgi.api.use_cases.manage_employees.AddEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddEmployeeTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AddEmployee addEmployee;

    private AddEmployeeRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new AddEmployeeRequest(
                " John ",
                " Doe",
                " john.doe@esgi.fr ",
                EmployeeRole.EMPLOYEE
        );
    }

    @Test
    void should_throw_conflict_when_email_already_exists() {
        when(employeeRepository.findByEmail("john.doe@esgi.fr")).thenReturn(Optional.of(mock(Employee.class)));

        ApiException exception = assertThrows(ApiException.class, () -> addEmployee.execute(validRequest));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Un employé existe déjà avec cet email.", exception.getMessage());

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void should_create_and_return_employee_on_success() {
        when(employeeRepository.findByEmail("john.doe@esgi.fr"))
                .thenReturn(Optional.empty());

        Employee toSave = Employee.create(validRequest);
        Employee savedEmployee = new Employee(
                UUID.randomUUID(),
                toSave.getLastName(),
                toSave.getFirstName(),
                toSave.getRole(),
                Collections.emptyList(),
                toSave.getEmail()
        );
        when(employeeRepository.save(any())).thenReturn(savedEmployee);

        AddEmployeeResponse response = addEmployee.execute(validRequest);

        assertNotNull(response);
        assertEquals(savedEmployee.getId(), response.employeeId());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("john.doe@esgi.fr", response.email());
        assertEquals(EmployeeRole.EMPLOYEE, response.role());

        verify(employeeRepository).save(argThat(employee -> employee.getFirstName().equals("John") && employee.getLastName().equals("Doe") && employee.getEmail().getValue().equals("john.doe@esgi.fr")));
    }

    @Test
    void should_trim_input_fields_before_saving() {
        AddEmployeeRequest requestWithSpaces = new AddEmployeeRequest(
                "  John  ",
                "  Doe ",
                "  john.doe@esgi.fr  ",
                EmployeeRole.MANAGER
        );
        when(employeeRepository.findByEmail("john.doe@esgi.fr")).thenReturn(Optional.empty());

        Employee toSave = Employee.create(requestWithSpaces);
        Employee savedEmployee = new Employee(UUID.randomUUID(), toSave.getLastName(), toSave.getFirstName(), toSave.getRole(), Collections.emptyList(), toSave.getEmail());
        when(employeeRepository.save(any())).thenReturn(savedEmployee);

        AddEmployeeResponse response = addEmployee.execute(requestWithSpaces);

        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("john.doe@esgi.fr", response.email());
        assertEquals(EmployeeRole.MANAGER, response.role());
    }

    @Test
    void should_throw_bad_request_when_domain_exception_occurs() {
        when(employeeRepository.findByEmail("john.doe@esgi.fr")).thenReturn(Optional.empty());
        when(employeeRepository.save(any())).thenThrow(new DomainException("donnée invalide"));

        ApiException ex = assertThrows(ApiException.class, () -> addEmployee.execute(validRequest));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("donnée invalide", ex.getMessage());

        verify(employeeRepository).findByEmail("john.doe@esgi.fr");
        verify(employeeRepository).save(any());
    }
}