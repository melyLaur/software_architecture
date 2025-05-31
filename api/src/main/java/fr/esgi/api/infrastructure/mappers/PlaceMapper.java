package fr.esgi.api.infrastructure.mappers;

import fr.esgi.api.infrastructure.jpa.entities.PlaceEntity;
import fr.esgi.api.model.reservation.place.Place;
import fr.esgi.api.model.reservation.place.PlaceIdentifier;
import fr.esgi.api.model.reservation.place.PlaceStatus;
import fr.esgi.api.model.reservation.place.PlaceType;
import org.springframework.stereotype.Service;

/**
 * Converts between PlaceEntity (JPA) and Place (domain) objects.
 */
@Service
public class PlaceMapper {
    /**
     * Map a JPA PlaceEntity into a domain Place object.
     *
     * @param entity the JPA PlaceEntity to convert
     * @return a populated domain Place object
     */
    public Place toDomain(PlaceEntity entity) {
        return new Place(
                entity.getId(),
                PlaceIdentifier.of(entity.getRow(), entity.getNumber()),
                PlaceType.valueOf(entity.getType()),
                PlaceStatus.valueOf(entity.getStatus())
        );
    }

    /**
     * Map a domain Place object into a JPA PlaceEntity for persistence.
     *
     * @param place the domain Place to convert
     * @return a new PlaceEntity ready to be saved
     */
    public PlaceEntity toEntity(Place place) {
        PlaceEntity entity = new PlaceEntity();
        entity.setNumber(place.getIdentifier().getNumber());
        entity.setRow(place.getIdentifier().getRow());
        entity.setStatus(place.getStatus().name());
        entity.setType(place.getType().name());
        return entity;
    }
}
