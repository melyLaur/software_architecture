package fr.esgi.api.infrastructure.jpa.repositories;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, UUID> {
}
