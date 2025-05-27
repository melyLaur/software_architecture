package fr.esgi.api.model.manage_employees;

import java.util.List;

public interface EmployeeRepository {
    List<Employee> getAll();
}
