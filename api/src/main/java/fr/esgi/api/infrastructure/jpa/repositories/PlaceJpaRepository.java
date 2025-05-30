package fr.esgi.api.infrastructure.jpa.repositories;

import fr.esgi.api.infrastructure.jpa.entities.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlaceJpaRepository extends JpaRepository<PlaceEntity, UUID> {
    List<PlaceEntity> findAllByStatusAndType(String status, String type);
}
