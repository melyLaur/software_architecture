package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.responses.DeleteEmployeeResponse;
import fr.esgi.api.model.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for removing an existing employee.
 */
@Service
public class DeleteEmployee {
    private final EmployeeRepository employeeRepository;

    public DeleteEmployee(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Deletes the employee identified by the given ID.
     *
     * @param employeeIdToDelete the ID of the employee to remove
     * @return an empty response indicating successful deletion
     */
    public DeleteEmployeeResponse execute(UUID employeeIdToDelete) {
        this.employeeRepository.deleteById(employeeIdToDelete);
        return new DeleteEmployeeResponse();
    }
}