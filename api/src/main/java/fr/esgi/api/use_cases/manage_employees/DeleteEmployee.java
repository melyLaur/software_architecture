package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.responses.DeleteEmployeeResponse;
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
        this.employeeRepository.deleteById(employeeIdToDelete);
        return new DeleteEmployeeResponse();
    }
}
