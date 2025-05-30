package fr.esgi.api.use_cases.manage_employee;

import fr.esgi.api.dtos.responses.GetEmployeeResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.model.employee.EmployeeRole;
import fr.esgi.api.model.employee.email.Email;
import fr.esgi.api.presentation.exceptions.ApiException;
import fr.esgi.api.use_cases.manage_employees.GetAllEmployees;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllEmployeesTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private GetAllEmployees getAllEmployees;

    private Employee alice;
    private Employee bob;

    @BeforeEach
    void setUp() {
        alice = new Employee(UUID.randomUUID(), "Dupont", "Alice", EmployeeRole.EMPLOYEE, Collections.emptyList(), Email.of("alice.dupont@example.com"));
        bob = new Employee(UUID.randomUUID(), "Martin", "Bob", EmployeeRole.MANAGER, Collections.emptyList(), Email.of("bob.martin@example.com"));
    }

    @Test
    void should_return_mapped_responses_when_repository_returns_employees() {
        when(employeeRepository.getAll()).thenReturn(List.of(alice, bob));

        List<GetEmployeeResponse> responses = getAllEmployees.getAll();

        assertEquals(2, responses.size());

        GetEmployeeResponse response1 = responses.getFirst();
        assertEquals(alice.getId(), response1.employeeId());
        assertEquals("Alice", response1.firstName());
        assertEquals("Dupont", response1.lastName());
        assertEquals("alice.dupont@example.com", response1.email());
        assertEquals(EmployeeRole.EMPLOYEE, response1.role());

        GetEmployeeResponse response2 = responses.get(1);
        assertEquals(bob.getId(), response2.employeeId());
        assertEquals("Bob", response2.firstName());
        assertEquals("Martin", response2.lastName());
        assertEquals("bob.martin@example.com", response2.email());
        assertEquals(EmployeeRole.MANAGER, response2.role());

        verify(employeeRepository).getAll();
    }

    @Test
    void should_throw_api_exception_when_domain_exception_occurs() {
        when(employeeRepository.getAll()).thenThrow(new DomainException("boom"));

        ApiException exception = assertThrows(ApiException.class, () -> getAllEmployees.getAll());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("boom", exception.getMessage());

        verify(employeeRepository).getAll();
    }
}