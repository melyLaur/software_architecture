package fr.esgi.api.model.reservation.place;

import java.util.List;

/**
 * Repository abstraction for Place persistence operations.
 */
public interface PlaceRepository {
    /**
     * Retrieves all available places of the specified type.
     *
     * @param placeType the type of places to filter (NORMAL, ELECTRICAL)
     * @return a list of available places matching the given type
     */
    List<Place> getAvailablePlaces(PlaceType placeType);
}
