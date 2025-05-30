package fr.esgi.api.infrastructure.jpa.repositories;

import fr.esgi.api.infrastructure.jpa.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, UUID> {
    boolean existsByPlace_IdAndStartDate(UUID placeId, LocalDate startDate);

    void deleteAllByCheckedInFalseAndStartDateAndEmployee_Role(LocalDate startDate, String role);
}
