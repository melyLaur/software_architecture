package fr.esgi.api.presentation;

import fr.esgi.api.dtos.requests.AddEmployeeRequest;
import fr.esgi.api.dtos.responses.AddEmployeeResponse;
import fr.esgi.api.dtos.responses.DeleteEmployeeResponse;
import fr.esgi.api.dtos.responses.GetEmployeeByIdResponse;
import fr.esgi.api.dtos.responses.GetEmployeeResponse;
import fr.esgi.api.use_cases.manage_employees.AddEmployee;
import fr.esgi.api.use_cases.manage_employees.DeleteEmployee;
import fr.esgi.api.use_cases.manage_employees.GetAllEmployees;
import fr.esgi.api.use_cases.manage_employees.GetEmployeeById;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*")
public class ManageUserController {
    private final GetAllEmployees getAllEmployees;
    private final AddEmployee addEmployee;
    private final DeleteEmployee deleteEmployee;
    private final GetEmployeeById getEmployeeById;

    public ManageUserController(GetAllEmployees getAllEmployees, AddEmployee addEmployee, DeleteEmployee deleteEmployee, GetEmployeeById getEmployeeById) {
        this.getAllEmployees = getAllEmployees;
        this.addEmployee = addEmployee;
        this.deleteEmployee = deleteEmployee;
        this.getEmployeeById = getEmployeeById;
    }

    @GetMapping
    public List<GetEmployeeResponse> getAllEmployees() {
        return this.getAllEmployees.getAll();
    }

    @PostMapping("/add-employee")
    public AddEmployeeResponse createEmployee(@Valid @RequestBody AddEmployeeRequest addEmployeeRequest) {
        return this.addEmployee.execute(addEmployeeRequest);
    }

    @DeleteMapping("/delete-employee")
    public DeleteEmployeeResponse deleteEmployee(@RequestParam UUID employeeIdToDelete) {
        return this.deleteEmployee.execute(employeeIdToDelete);
    }

    @GetMapping("/{employeeId}")
    public GetEmployeeByIdResponse getEmployeeById(@PathVariable UUID employeeId) {
        return this.getEmployeeById.execute(employeeId);
    }
}
