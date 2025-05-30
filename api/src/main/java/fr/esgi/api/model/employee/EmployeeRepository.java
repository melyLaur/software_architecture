package fr.esgi.api.model.employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {
    List<Employee> getAll();

    Employee getById(UUID employeeId) throws EmployeeNotFoundException;
    Employee save(Employee employee) throws EmployeeNotFoundException;
    void delete(Employee employee) throws EmployeeNotFoundException;
    Optional<Employee> findByEmail(String email);
}
