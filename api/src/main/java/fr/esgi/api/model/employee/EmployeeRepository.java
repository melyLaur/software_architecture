package fr.esgi.api.model.employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository abstraction for Employee persistence operations.
 */
public interface EmployeeRepository {

    /**
     * Retrieve all employees.
     *
     * @return list of all employees
     */
    List<Employee> getAll();

    /**
     * Find an employee by its unique identifier.
     *
     * @param employeeId the UUID of the employee
     * @return the matching Employee
     * @throws EmployeeNotFoundException if no employee is found
     */
    Employee getById(UUID employeeId) throws EmployeeNotFoundException;

    /**
     * Save a new or updated employee.
     *
     * @param employee the employee to persist
     * @return the saved employee with assigned ID
     * @throws EmployeeNotFoundException never thrown when creating,
     *                                   but may be used for update failures
     */
    Employee save(Employee employee) throws EmployeeNotFoundException;

    /**
     * Delete an employee by its unique identifier.
     *
     * @param employeeId the UUID of the employee to delete
     * @throws EmployeeNotFoundException if no employee is found with the given ID
     */
    void deleteById(UUID employeeId) throws EmployeeNotFoundException;

    /**
     * Find an employee by email address.
     *
     * @param email the email to search
     * @return an Optional containing the employee if found, or empty otherwise
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Update an existing employee.
     *
     * @param employee the employee with updated information
     * @return the updated Employee object
     * @throws EmployeeNotFoundException if the employee does not exist
     */
    Employee update(Employee employee) throws EmployeeNotFoundException;
}