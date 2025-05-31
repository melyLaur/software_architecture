package fr.esgi.api.infrastructure.jpa.repositories;

import fr.esgi.api.infrastructure.jpa.entities.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for PLaceEntity
 */
public interface PlaceJpaRepository extends JpaRepository<PlaceEntity, UUID> {
    /**
     * Finds all places matching the given status and type.
     *
     * @param status the current status of the place (AVAILABLE, UNAVAILABLE)
     * @param type the type of the place (NORMAL, ELECTRICAL)
     * @return a list of matching places
     */
    List<PlaceEntity> findAllByStatusAndType(String status, String type);
}
