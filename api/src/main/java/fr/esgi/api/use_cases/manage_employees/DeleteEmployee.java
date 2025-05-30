package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.responses.DeleteEmployeeResponse;
import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeNotFoundException;
import fr.esgi.api.model.reservation.employee.EmployeeRepository;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import fr.esgi.api.model.reservation.exceptions.UnauthorizedEmployeeCreationException;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteEmployee {
    private final EmployeeRepository employeeRepository;

    public DeleteEmployee(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public DeleteEmployeeResponse execute(UUID creatorId, UUID employeeIdToDelete){
        Employee creator = this.employeeRepository.getById(creatorId);
        if (creator == null) {
            throw new EmployeeNotFoundException();
        }

        if (creator.getRole() != EmployeeRole.SECRETARY) {
            throw new UnauthorizedEmployeeCreationException();
        }

        try {
            Employee employeeToDelete = this.employeeRepository.getById(employeeIdToDelete);

            this.employeeRepository.delete(employeeToDelete);
            return new DeleteEmployeeResponse();
        } catch (EmployeeNotFoundException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
