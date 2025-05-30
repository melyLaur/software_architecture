package fr.esgi.api.model.employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {
    List<Employee> getAll();

    Employee getById(UUID employeeId) throws EmployeeNotFoundException;
    Employee save(Employee employee) throws EmployeeNotFoundException;
    void deleteById(UUID employeeId) throws EmployeeNotFoundException;
    Optional<Employee> findByEmail(String email);
    Employee update(Employee employee) throws EmployeeNotFoundException;
}
