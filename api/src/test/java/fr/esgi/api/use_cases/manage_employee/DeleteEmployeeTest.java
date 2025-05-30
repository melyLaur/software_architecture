package fr.esgi.api.use_cases.manage_employee;

import fr.esgi.api.dtos.responses.DeleteEmployeeResponse;
import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeNotFoundException;
import fr.esgi.api.model.reservation.employee.EmployeeRepository;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import fr.esgi.api.model.reservation.employee.email.Email;
import fr.esgi.api.model.reservation.exceptions.UnauthorizedEmployeeCreationException;
import fr.esgi.api.presentation.exceptions.ApiException;
import fr.esgi.api.use_cases.manage_employees.DeleteEmployee;
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
class DeleteEmployeeTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DeleteEmployee deleteEmployee;

    private Employee secretary;
    private Employee employeeToDelete;

    @BeforeEach
    void setup() {
        secretary = new Employee(UUID.randomUUID(), "Laura", "Dumont", EmployeeRole.SECRETARY, Collections.emptyList(), Email.of("laura.secretary@esgi.fr"));
        employeeToDelete = new Employee(UUID.randomUUID(), "John","Doe", EmployeeRole.EMPLOYEE, Collections.emptyList(), Email.of("john.doe@esgi.fr"));
    }

    @Test
    void should_delete_employee_when_creator_is_secretary() {
        when(employeeRepository.getById(secretary.getId())).thenReturn(secretary);
        when(employeeRepository.getById(employeeToDelete.getId())).thenReturn(employeeToDelete);

        DeleteEmployeeResponse response = deleteEmployee.execute(secretary.getId(), employeeToDelete.getId());

        assertNotNull(response);
        verify(employeeRepository).delete(employeeToDelete);
    }

    @Test
    void should_throw_when_creator_is_not_secretary() {
        Employee nonSecretary = new Employee(UUID.randomUUID(), "Etienne", "Giraud", EmployeeRole.EMPLOYEE, Collections.emptyList(), Email.of("etienne.employee@esgi.fr"));

        when(employeeRepository.getById(nonSecretary.getId())).thenReturn(nonSecretary);

        assertThrows(UnauthorizedEmployeeCreationException.class, () -> deleteEmployee.execute(nonSecretary.getId(), employeeToDelete.getId()));
        verify(employeeRepository, never()).delete(any());
    }

    @Test
    void should_throw_when_employee_to_delete_not_found() {
        when(employeeRepository.getById(secretary.getId())).thenReturn(secretary);
        when(employeeRepository.getById(employeeToDelete.getId())).thenThrow(new EmployeeNotFoundException());

        assertThrows(ApiException.class, () -> deleteEmployee.execute(secretary.getId(), employeeToDelete.getId()));
        verify(employeeRepository, never()).delete(any());
    }

    @Test
    void should_throw_when_creator_not_found() {
        UUID invalidCreatorId = UUID.randomUUID();
        when(employeeRepository.getById(invalidCreatorId)).thenThrow(new EmployeeNotFoundException());

        assertThrows(EmployeeNotFoundException.class, () -> deleteEmployee.execute(invalidCreatorId, employeeToDelete.getId()));
        verify(employeeRepository, never()).delete(any());
    }
}