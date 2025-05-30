package fr.esgi.api.use_cases.manage_employee;

import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeNotFoundException;
import fr.esgi.api.model.reservation.employee.EmployeeRepository;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import fr.esgi.api.model.reservation.employee.email.Email;
import fr.esgi.api.presentation.exceptions.ApiException;
import fr.esgi.api.use_cases.manage_employees.GetEmployeeById;
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
class GetEmployeeByIdTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private GetEmployeeById getEmployeeById;

    @Test
    void should_return_employee_when_exists() {
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee(employeeId, "Smith", "Anna", EmployeeRole.SECRETARY, Collections.emptyList(), Email.of("anna@esgi.fr"));

        when(employeeRepository.getById(employeeId)).thenReturn(employee);

        Employee result = getEmployeeById.execute(employeeId);

        assertNotNull(result);
        assertEquals("Smith", result.getLastName());
        verify(employeeRepository).getById(employeeId);
    }

    @Test
    void should_throw_when_employee_not_found() {
        UUID employeeId = UUID.randomUUID();
        when(employeeRepository.getById(employeeId)).thenThrow(new EmployeeNotFoundException());

        assertThrows(ApiException.class, () -> getEmployeeById.execute(employeeId));
        verify(employeeRepository).getById(employeeId);
    }
}