package fr.esgi.api.presentation;

import fr.esgi.api.model.manage_employees.Employee;
import fr.esgi.api.use_cases.manage_employees.GetAllEmployees;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*")
public class ManageUserController {
    private final GetAllEmployees getAllEmployees;

    public ManageUserController(GetAllEmployees getAllEmployees) {
        this.getAllEmployees = getAllEmployees;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return this.getAllEmployees.getAll();
    }
}
