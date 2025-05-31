package fr.esgi.api.presentation;

import fr.esgi.api.dtos.requests.AddEmployeeRequest;
import fr.esgi.api.dtos.requests.UpdateEmployeeRequest;
import fr.esgi.api.dtos.responses.AddEmployeeResponse;
import fr.esgi.api.dtos.responses.DeleteEmployeeResponse;
import fr.esgi.api.dtos.responses.GetEmployeeResponse;
import fr.esgi.api.dtos.responses.UpdateEmployeeResponse;
import fr.esgi.api.use_cases.manage_employees.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for employee management endpoints.
 */
@RestController
@CrossOrigin(origins = "*")
public class ManageUserController {
    private final GetAllEmployees getAllEmployees;
    private final AddEmployee addEmployee;
    private final DeleteEmployee deleteEmployee;
    private final GetEmployeeById getEmployeeById;
    private final UpdateEmployee updateEmployee;

    public ManageUserController(GetAllEmployees getAllEmployees, AddEmployee addEmployee, DeleteEmployee deleteEmployee, GetEmployeeById getEmployeeById, UpdateEmployee updateEmployee) {
        this.getAllEmployees = getAllEmployees;
        this.addEmployee = addEmployee;
        this.deleteEmployee = deleteEmployee;
        this.getEmployeeById = getEmployeeById;
        this.updateEmployee = updateEmployee;
    }

    /**
     * Retrieves all employees.
     *
     * @return list of employee DTOs
     */
    @GetMapping("/employees")
    public List<GetEmployeeResponse> getAllEmployees() {
        return this.getAllEmployees.getAll();
    }

    /**
     * Creates a new employee.
     *
     * @param addEmployeeRequest incoming data for the new employee
     * @return DTO of the created employee
     */
    @PostMapping("/employees")
    public AddEmployeeResponse createEmployee(@Valid @RequestBody AddEmployeeRequest addEmployeeRequest) {
        return this.addEmployee.execute(addEmployeeRequest);
    }

    /**
     * Deletes an existing employee.
     *
     * @param id the ID of the employee to remove
     * @return empty response indicating success
     */
    @DeleteMapping("/employees/{id}")
    public DeleteEmployeeResponse deleteEmployee(@PathVariable UUID id) {
        return this.deleteEmployee.execute(id);
    }

    /**
     * Retrieves a single employee by ID.
     *
     * @param id the ID of the employee to fetch
     * @return DTO of the requested employee
     */
    @GetMapping("/employees/{id}")
    public GetEmployeeResponse getEmployeeById(@PathVariable UUID id) {
        return this.getEmployeeById.execute(id);
    }

    /**
     * Updates an existing employee's information.
     *
     * @param id the UUID of the employee to update
     * @param updateEmployeeRequest the updated employee information
     * @return DTO of the updated employee
     */
    @PutMapping("/employees/{id}")
    public UpdateEmployeeResponse updateEmployee(@PathVariable UUID id, @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        return this.updateEmployee.execute(id, updateEmployeeRequest);
    }
}