package fr.esgi.api.infrastructure.jpa.repositories;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import fr.esgi.api.infrastructure.jpa.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Spring Data JPA repository for ReservationEntity
 * <p>
 * Provides standard CRUD operations and custom queries related to reservations.
 */
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, UUID> {
    /**
     * Checks if a reservation exists for a specific place and date.
     *
     * @param placeId the UUID of the place
     * @param startDate the date of the reservation
     * @return true if a reservation exists, false otherwise
     */
    boolean existsByPlace_IdAndBookedFor(UUID placeId, LocalDate startDate);

    /**
     * Checks if an employee already has a reservation for a given date.
     *
     * @param employee the employee entity
     * @param bookedFor the date to check
     * @return true if a reservation exists for that employee and date, false otherwise
     */
    boolean existsByEmployeeAndBookedFor(EmployeeEntity employee, LocalDate bookedFor);

    /**
     * Deletes all reservations that match the following criteria:
     * - not checked in
     * - match the specified booking date
     * - belong to employees with the given role
     *
     * @param startDate the booking date to match
     * @param role the role of the employees (e.g., EMPLOYEE)
     */
    void deleteAllByCheckedInFalseAndBookedForAndEmployee_Role(LocalDate startDate, String role);
}
