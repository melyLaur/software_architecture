package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.responses.DeleteEmployeeResponse;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteEmployee {
    private final EmployeeRepository employeeRepository;

    public DeleteEmployee(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public DeleteEmployeeResponse execute(UUID employeeIdToDelete) {
        Employee employeeToDelete = this.employeeRepository.getById(employeeIdToDelete);
        this.employeeRepository.delete(employeeToDelete);
        return new DeleteEmployeeResponse();
    }
}
