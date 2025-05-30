package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.dtos.requests.AddEmployeeRequest;
import fr.esgi.api.dtos.responses.AddEmployeeResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeAlreadyExist;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AddEmployee {
    private final EmployeeRepository employeeRepository;

    public AddEmployee(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public AddEmployeeResponse execute(AddEmployeeRequest request) {
        try {
            if (employeeRepository.findByEmail(request.email().trim()).isPresent()) {
                throw new EmployeeAlreadyExist();
            }

            Employee toCreate = Employee.create(request);
            Employee saved = employeeRepository.save(toCreate);

            return new AddEmployeeResponse(
                    saved.getId(),
                    saved.getFirstName(),
                    saved.getLastName(),
                    saved.getEmail().getValue(),
                    saved.getRole()
            );
        } catch (EmployeeAlreadyExist e) {
            throw new ApiException(HttpStatus.CONFLICT, e.getMessage());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
