package fr.esgi.api.model.reservation.place;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a place (parking spot) in the reservation system.
 * Each place has a unique identifier, a physical identifier (row/number),
 * a type (NORMAL, ELECTRICAL), and a current status (AVAILABLE, UNAVAILABLE).
 */
public class Place {
    private UUID id;
    private PlaceIdentifier identifier;
    private PlaceType type;
    private PlaceStatus status;

    public Place(UUID id, PlaceIdentifier identifier, PlaceType type, PlaceStatus status) {
        this.id = id;
        this.identifier = identifier;
        this.type = type;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PlaceIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(PlaceIdentifier identifier) {
        this.identifier = identifier;
    }

    public PlaceType getType() {
        return type;
    }

    public void setType(PlaceType type) {
        this.type = type;
    }

    public PlaceStatus getStatus() {
        return status;
    }

    public void setStatus(PlaceStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(identifier, place.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }
}
