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

    @GetMapping("/employees")
    public List<GetEmployeeResponse> getAllEmployees() {
        return this.getAllEmployees.getAll();
    }

    @PostMapping("/employees")
    public AddEmployeeResponse createEmployee(@Valid @RequestBody AddEmployeeRequest addEmployeeRequest) {
        return this.addEmployee.execute(addEmployeeRequest);
    }

    @DeleteMapping("/employees/{id}")
    public DeleteEmployeeResponse deleteEmployee(@PathVariable UUID id) {
        return this.deleteEmployee.execute(id);
    }

    @GetMapping("/employees/{id}")
    public GetEmployeeResponse getEmployeeById(@PathVariable UUID id) {
        return this.getEmployeeById.execute(id);
    }

    @PutMapping("/employees/{id}")
    public UpdateEmployeeResponse updateEmployee(@PathVariable UUID id, @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        return this.updateEmployee.execute(id, updateEmployeeRequest);
    }
}
