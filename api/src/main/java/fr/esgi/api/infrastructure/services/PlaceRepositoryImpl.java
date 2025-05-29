package fr.esgi.api.infrastructure.services;

import fr.esgi.api.infrastructure.jpa.entities.PlaceEntity;
import fr.esgi.api.infrastructure.jpa.repositories.PlaceJpaRepository;
import fr.esgi.api.infrastructure.mappers.PlaceMapper;
import fr.esgi.api.model.reservation.place.Place;
import fr.esgi.api.model.reservation.place.PlaceRepository;
import fr.esgi.api.model.reservation.place.PlaceStatus;
import fr.esgi.api.model.reservation.place.PlaceType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceRepositoryImpl implements PlaceRepository {
    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceMapper placeMapper;

    public PlaceRepositoryImpl(PlaceJpaRepository placeJpaRepository, PlaceMapper placeMapper) {
        this.placeJpaRepository = placeJpaRepository;
        this.placeMapper = placeMapper;
    }

    @Override
    public List<Place> getAvailablePlaces(PlaceType type) {
        List<PlaceEntity> places = this.placeJpaRepository.findAllByStatusAndType(PlaceStatus.AVAILABLE.name(), type.name());
        return places.stream().map(placeMapper::toDomain).toList();
    }
}
