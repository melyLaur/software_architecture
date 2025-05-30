package fr.esgi.api.use_cases.manage_employee;

import fr.esgi.api.dtos.responses.GetEmployeeByIdResponse;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeNotFoundException;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.model.employee.EmployeeRole;
import fr.esgi.api.model.employee.email.Email;
import fr.esgi.api.presentation.exceptions.ApiException;
import fr.esgi.api.use_cases.manage_employees.GetEmployeeById;
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
class GetEmployeeByIdTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private GetEmployeeById getEmployeeById;

    @Test
    void should_return_response_with_employee_id_when_employee_exists() {
        UUID employeeId = UUID.randomUUID();
        Employee existingEmployee = new Employee(employeeId, "Smith", "Anna", EmployeeRole.SECRETARY, Collections.emptyList(), Email.of("anna@esgi.fr"));
        when(employeeRepository.getById(employeeId)).thenReturn(existingEmployee);
        GetEmployeeByIdResponse response = getEmployeeById.execute(employeeId);

        assertNotNull(response);
        assertEquals(employeeId, response.employeeId());
        verify(employeeRepository).getById(employeeId);
    }

    @Test
    void should_throw_api_exception_not_found_when_employee_does_not_exist() {
        UUID missingEmployeeId = UUID.randomUUID();
        when(employeeRepository.getById(missingEmployeeId)).thenThrow(new EmployeeNotFoundException());

        ApiException exception = assertThrows(ApiException.class, () -> getEmployeeById.execute(missingEmployeeId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(employeeRepository).getById(missingEmployeeId);
    }
}