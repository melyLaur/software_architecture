package fr.esgi.api.use_cases.manage_employee;

import fr.esgi.api.dtos.requests.UpdateEmployeeRequest;
import fr.esgi.api.dtos.responses.UpdateEmployeeResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.employee.*;
import fr.esgi.api.model.employee.email.Email;
import fr.esgi.api.presentation.exceptions.ApiException;
import fr.esgi.api.use_cases.manage_employees.UpdateEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateEmployeeTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private UpdateEmployee updateEmployee;

    private UUID employeeId;
    private UpdateEmployeeRequest validRequest;
    private Employee existingEmployee;

    @BeforeEach
    void setUp() {
        employeeId = UUID.randomUUID();

        validRequest = new UpdateEmployeeRequest(
                " Jane ",
                " Doe ",
                EmployeeRole.MANAGER,
                " jane.doe@esgi.fr "
        );

        existingEmployee = new Employee(
                employeeId,
                "OldLastName",
                "OldFirstName",
                EmployeeRole.EMPLOYEE,
                Collections.emptyList(),
                Email.of("old.email@esgi.fr")
        );
    }

    @Test
    void should_throw_not_found_when_employee_does_not_exist() {
        when(employeeRepository.getById(employeeId)).thenThrow(new EmployeeNotFoundException());

        ApiException exception = assertThrows(ApiException.class, () -> updateEmployee.execute(employeeId, validRequest));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(employeeRepository, never()).update(any());
    }

    @Test
    void should_update_and_return_employee_on_success() {
        when(employeeRepository.getById(employeeId)).thenReturn(existingEmployee);

        Employee updatedEmployee = new Employee(
                employeeId,
                "Doe",
                "Jane",
                EmployeeRole.MANAGER,
                Collections.emptyList(),
                Email.of("jane.doe@esgi.fr")
        );
        when(employeeRepository.update(any())).thenReturn(updatedEmployee);

        UpdateEmployeeResponse response = updateEmployee.execute(employeeId, validRequest);

        assertNotNull(response);
        assertEquals(employeeId, response.employeeId());
        assertEquals("Jane", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("jane.doe@esgi.fr", response.email());
        assertEquals(EmployeeRole.MANAGER, response.role());

        verify(employeeRepository).update(any(Employee.class));
    }

    @Test
    void should_trim_input_fields_before_updating() {
        when(employeeRepository.getById(employeeId)).thenReturn(existingEmployee);

        Employee updatedEmployee = new Employee(
                employeeId,
                "Doe",
                "Jane",
                EmployeeRole.MANAGER,
                Collections.emptyList(),
                Email.of("jane.doe@esgi.fr")
        );
        when(employeeRepository.update(any())).thenReturn(updatedEmployee);

        UpdateEmployeeResponse response = updateEmployee.execute(employeeId, validRequest);

        assertEquals("Jane", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("jane.doe@esgi.fr", response.email());
    }

    @Test
    void should_throw_bad_request_when_domain_exception_occurs() {
        when(employeeRepository.getById(employeeId)).thenReturn(existingEmployee);
        doThrow(new DomainException("invalide")).when(employeeRepository).update(any());

        ApiException ex = assertThrows(ApiException.class, () -> updateEmployee.execute(employeeId, validRequest));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("invalide", ex.getMessage());

        verify(employeeRepository).getById(employeeId);
        verify(employeeRepository).update(any());
    }
}