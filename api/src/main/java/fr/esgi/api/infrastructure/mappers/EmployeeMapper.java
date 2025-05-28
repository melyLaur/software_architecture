package fr.esgi.api.infrastructure.mappers;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeMapper {
    public Employee toDomain(EmployeeEntity entity, List<Reservation> reservations) {
        return new Employee(entity.getId(),
                entity.getLastName(),
                entity.getFirstName(),
                EmployeeRole.valueOf(entity.getRole()),
                reservations
        );
    }

    public EmployeeEntity toEntity(Employee employee) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setFirstName(employee.getFirstName());
        entity.setLastName(employee.getLastName());
        entity.setRole(employee.getRole().name());
        return entity;
    }
}
