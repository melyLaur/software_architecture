package fr.esgi.api.model.reservation.place;

import java.util.List;

public interface PlaceRepository {
    List<Place> getAvailablePlaces(PlaceType placeType);
}
