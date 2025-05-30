package fr.esgi.api.model.reservation.employee;

import fr.esgi.api.dtos.requests.AddEmployeeRequest;
import fr.esgi.api.model.reservation.exceptions.UnauthorizedEmployeeCreationException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee creator, AddEmployeeRequest addEmployeeRequest) {
        if (creator.getRole() != EmployeeRole.SECRETARY) {
            throw new UnauthorizedEmployeeCreationException();
        }
        if (this.employeeRepository.findByEmail(addEmployeeRequest.email()).isPresent()) {
            throw new EmployeeAlreadyExist();
        }
        return Employee.create(addEmployeeRequest);
    }
}