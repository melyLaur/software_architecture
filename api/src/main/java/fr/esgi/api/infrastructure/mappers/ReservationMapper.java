package fr.esgi.api.infrastructure.mappers;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import fr.esgi.api.infrastructure.jpa.entities.PlaceEntity;
import fr.esgi.api.infrastructure.jpa.entities.ReservationEntity;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.place.Place;
import org.springframework.stereotype.Service;

@Service
public class ReservationMapper {
    public Reservation toDomain(ReservationEntity entity, Employee employee, Place place) {
        return new Reservation(
                entity.getId(),
                employee,
                place,
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isCheckedIn()
        );
    }

    public Reservation toDomain(ReservationEntity entity, Place place) {
        return new Reservation(
                entity.getId(),
                place,
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isCheckedIn()
        );
    }


    public ReservationEntity toEntity(Reservation reservation, EmployeeEntity employeeEntity, PlaceEntity placeEntity) {
        ReservationEntity entity = new ReservationEntity();
        entity.setEmployee(employeeEntity);
        entity.setPlace(placeEntity);
        entity.setStartDate(reservation.getStartDate());
        entity.setEndDate(reservation.getEndDate());
        entity.setCheckedIn(reservation.isCheckedIn());
        return entity;
    }
}
