package fr.esgi.api.infrastructure.mappers;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import fr.esgi.api.model.manage_employees.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper {
    public Employee toDomain(EmployeeEntity entity) {
        return new Employee(entity.getId(), entity.getLastName(), entity.getFirstName());
    }
}
