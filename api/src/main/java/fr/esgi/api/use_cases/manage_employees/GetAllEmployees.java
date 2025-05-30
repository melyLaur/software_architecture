package fr.esgi.api.use_cases.manage_employees;

import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllEmployees {
    private final EmployeeRepository employeeRepository;

    public GetAllEmployees(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll() {
        return this.employeeRepository.getAll();
    }

}
