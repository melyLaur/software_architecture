package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.requests.AddEmployeeRequest;
import fr.esgi.api.dtos.responses.AddEmployeeResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.reservation.employee.*;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddEmployee {
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeDomainService;

    public AddEmployee(EmployeeRepository employeeRepo, EmployeeService employeeDomainService) {
        this.employeeRepository = employeeRepo;
        this.employeeDomainService = employeeDomainService;
    }

    public AddEmployeeResponse execute(UUID creatorId, AddEmployeeRequest dto) {
        try {
            Employee creator = this.employeeRepository.getById(creatorId);
            Employee newEmployee = this.employeeDomainService.createEmployee(creator, dto);
            Employee savedEmployee = this.employeeRepository.save(newEmployee);

            return new AddEmployeeResponse(
                    savedEmployee.getId(),
                    savedEmployee.getFirstName(),
                    savedEmployee.getLastName(),
                    savedEmployee.getEmail().getValue()
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
