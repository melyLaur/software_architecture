package fr.esgi.api.infrastructure.mappers;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import fr.esgi.api.infrastructure.jpa.entities.PlaceEntity;
import fr.esgi.api.infrastructure.jpa.entities.ReservationEntity;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.reservation.place.Place;
import org.springframework.stereotype.Service;

/**
 * Converts between ReservationEntity (JPA) and Reservation (domain) objects.
 */
@Service
public class ReservationMapper {
    /**
     * Maps a JPA ReservationEntity to a domain Reservation,
     * including both employee and place.
     *
     * @param entity   the JPA ReservationEntity
     * @param employee the domain Employee associated with the reservation
     * @param place    the domain Place associated with the reservation
     * @return a populated domain Reservation
     */
    public Reservation toDomain(ReservationEntity entity, Employee employee, Place place) {
        return new Reservation(
                entity.getId(),
                employee,
                place,
                entity.getBookedFor(),
                entity.checkedIn()
        );
    }

    /**
     * Maps a JPA ReservationEntity to a domain Reservation,
     * when the employee is not needed in the domain context.
     *
     * @param entity the JPA ReservationEntity
     * @param place  the domain Place associated with the reservation
     * @return a domain Reservation without employee context
     */
    public Reservation toDomain(ReservationEntity entity, Place place) {
        return new Reservation(
                entity.getId(),
                place,
                entity.getBookedFor(),
                entity.checkedIn()
        );
    }

    /**
     * Maps a domain Reservation to a JPA ReservationEntity for persistence.
     * <p>
     * Requires both the JPA EmployeeEntity and PlaceEntity to be passed explicitly.
     *
     * @param reservation   the domain Reservation to convert
     * @param employeeEntity the JPA EmployeeEntity to link
     * @param placeEntity    the JPA PlaceEntity to link
     * @return a new ReservationEntity ready to persist
     */
    public ReservationEntity toEntity(Reservation reservation, EmployeeEntity employeeEntity, PlaceEntity placeEntity) {
        ReservationEntity entity = new ReservationEntity();
        entity.setEmployee(employeeEntity);
        entity.setPlace(placeEntity);
        entity.setBookedFor(reservation.getBookedFor());
        entity.setCheckedIn(reservation.isCheckedIn());
        return entity;
    }
}
