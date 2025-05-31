package fr.esgi.api.infrastructure.jpa.repositories;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for EmployeeEntity
 * Provides basic CRUD operations and a finder by email.
 */
public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, UUID> {

    /**
     * Find an employee entity by its email address.
     *
     * @param email the email to search for
     * @return an Optional containing the matching entity if found, or empty otherwise
     */
    Optional<EmployeeEntity> findByEmail(String email);
}