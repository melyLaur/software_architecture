package fr.esgi.api.use_cases.manage_employee;

import fr.esgi.api.dtos.responses.DeleteEmployeeResponse;
import fr.esgi.api.model.employee.EmployeeNotFoundException;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.use_cases.manage_employees.DeleteEmployee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteEmployeeTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DeleteEmployee deleteEmployee;

    @Test
    void should_delete_existing_employee_and_return_response() {
        UUID employeeId = UUID.randomUUID();

        DeleteEmployeeResponse response = deleteEmployee.execute(employeeId);

        assertNotNull(response);
        verify(employeeRepository).deleteById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void should_throw_when_employee_not_found() {
        UUID missingEmployeeId = UUID.randomUUID();
        doThrow(new EmployeeNotFoundException()).when(employeeRepository).deleteById(missingEmployeeId);

        assertThrows(EmployeeNotFoundException.class, () -> deleteEmployee.execute(missingEmployeeId));

        verify(employeeRepository).deleteById(missingEmployeeId);
        verifyNoMoreInteractions(employeeRepository);
    }
}