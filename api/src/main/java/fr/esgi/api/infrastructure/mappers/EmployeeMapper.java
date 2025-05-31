package fr.esgi.api.infrastructure.mappers;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeRole;
import fr.esgi.api.model.employee.email.Email;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Converts between EmployeeEntity (JPA) and Employee (domain) objects.
 */
@Service
public class EmployeeMapper {
    /**
     * Map a JPA entity and its reservations into a domain Employee.
     *
     * @param entity       the JPA EmployeeEntity
     * @param reservations domain Reservation list
     * @return populated Employee domain object
     */
    public Employee toDomain(EmployeeEntity entity, List<Reservation> reservations) {
        return new Employee(entity.getId(),
                entity.getLastName(),
                entity.getFirstName(),
                EmployeeRole.valueOf(entity.getRole()),
                reservations,
                Email.of(entity.getEmail())
        );
    }

    /**
     * Map a domain Employee into a JPA entity for persistence.
     *
     * @param employee domain Employee
     * @return EmployeeEntity ready to save
     */
    public EmployeeEntity toEntity(Employee employee) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setFirstName(employee.getFirstName());
        entity.setLastName(employee.getLastName());
        entity.setRole(employee.getRole().name());
        entity.setEmail(employee.getEmail().getValue());
        return entity;
    }
}
