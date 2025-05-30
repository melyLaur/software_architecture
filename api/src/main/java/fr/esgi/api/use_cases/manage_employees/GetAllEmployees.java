package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.responses.GetEmployeeResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllEmployees {
    private final EmployeeRepository employeeRepository;

    public GetAllEmployees(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<GetEmployeeResponse> getAll() {
        try {
            List<Employee> employees = employeeRepository.getAll();
            return employees.stream().map(employee -> new GetEmployeeResponse(
                    employee.getId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getEmail().getValue(),
                    employee.getRole()
                    ))
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
