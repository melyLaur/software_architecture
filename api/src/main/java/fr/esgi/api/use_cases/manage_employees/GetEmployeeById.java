package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.responses.GetEmployeeByIdResponse;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeNotFoundException;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetEmployeeById {
    private final EmployeeRepository employeeRepository;

    public GetEmployeeById(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public GetEmployeeByIdResponse execute(UUID employeeId) {
        try {
            Employee employee = this.employeeRepository.getById(employeeId);
            return new GetEmployeeByIdResponse(employee.getId());
        } catch (EmployeeNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}