package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.requests.UpdateEmployeeRequest;
import fr.esgi.api.dtos.responses.UpdateEmployeeResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeNotFoundException;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.model.employee.email.Email;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for updating an existing employee's information.
 */
@Service
public class UpdateEmployee {
    private final EmployeeRepository employeeRepository;

    public UpdateEmployee(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Executes the update of an employee with the given ID.
     *
     * @param employeeId the UUID of the employee to update
     * @param updateEmployeeRequest the new values for the employee
     * @return the updated employee in response format
     * @throws ApiException with 404 if the employee does not exist
     * @throws ApiException with 400 if domain validation fails
     */
    public UpdateEmployeeResponse execute(UUID employeeId, UpdateEmployeeRequest updateEmployeeRequest) {
        try {
            Employee employee = this.employeeRepository.getById(employeeId);

            employee.update(
                    updateEmployeeRequest.firstName(),
                    updateEmployeeRequest.lastName(),
                    updateEmployeeRequest.role(),
                    Email.of(updateEmployeeRequest.email())
            );

            Employee savedEmployee = this.employeeRepository.update(employee);
            return new UpdateEmployeeResponse(
                    savedEmployee.getId(),
                    savedEmployee.getFirstName(),
                    savedEmployee.getLastName(),
                    savedEmployee.getRole(),
                    savedEmployee.getEmail().getValue()
            );
        } catch (EmployeeNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}