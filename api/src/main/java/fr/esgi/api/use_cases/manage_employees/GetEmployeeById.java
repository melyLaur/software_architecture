package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.responses.GetEmployeeResponse;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeNotFoundException;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for retrieving a single employee by their ID.
 */
@Service
public class GetEmployeeById {
    private final EmployeeRepository employeeRepository;

    public GetEmployeeById(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Fetches the employee with the given ID and maps to a response DTO.
     *
     * @param employeeId the UUID of the employee to retrieve
     * @return response DTO containing the employee's data
     * @throws ApiException with 404 status if no such employee exists
     */
    public GetEmployeeResponse execute(UUID employeeId) {
        try {
            Employee employee = this.employeeRepository.getById(employeeId);
            return new GetEmployeeResponse(
                    employee.getId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getEmail().getValue(),
                    employee.getRole()
            );
        } catch (EmployeeNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}