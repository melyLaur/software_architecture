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

@Service
public class UpdateEmployee {
    private final EmployeeRepository employeeRepository;

    public UpdateEmployee(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

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