package fr.esgi.api.infrastructure.mappers;

import fr.esgi.api.infrastructure.jpa.entities.PlaceEntity;
import fr.esgi.api.model.reservation.place.Place;
import fr.esgi.api.model.reservation.place.PlaceIdentifier;
import fr.esgi.api.model.reservation.place.PlaceStatus;
import fr.esgi.api.model.reservation.place.PlaceType;
import org.springframework.stereotype.Service;

@Service
public class PlaceMapper {
    public Place toDomain(PlaceEntity entity) {
        return new Place(
                entity.getId(),
                PlaceIdentifier.of(entity.getRow(), entity.getNumber()),
                PlaceType.valueOf(entity.getType()),
                PlaceStatus.valueOf(entity.getStatus())
        );
    }

    public PlaceEntity toEntity(Place place) {
        PlaceEntity entity = new PlaceEntity();
        entity.setNumber(place.getIdentifier().getNumber());
        entity.setRow(place.getIdentifier().getRow());
        entity.setStatus(place.getStatus().name());
        entity.setType(place.getType().name());
        return entity;
    }
}
