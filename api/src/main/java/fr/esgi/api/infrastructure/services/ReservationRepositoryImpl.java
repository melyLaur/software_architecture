package fr.esgi.api.infrastructure.services;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import fr.esgi.api.infrastructure.jpa.entities.PlaceEntity;
import fr.esgi.api.infrastructure.jpa.entities.ReservationEntity;
import fr.esgi.api.infrastructure.jpa.repositories.EmployeeJpaRepository;
import fr.esgi.api.infrastructure.jpa.repositories.PlaceJpaRepository;
import fr.esgi.api.infrastructure.jpa.repositories.ReservationJpaRepository;
import fr.esgi.api.infrastructure.mappers.EmployeeMapper;
import fr.esgi.api.infrastructure.mappers.PlaceMapper;
import fr.esgi.api.infrastructure.mappers.ReservationMapper;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeNotFoundException;
import fr.esgi.api.model.employee.EmployeeRole;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.reservation.ReservationRepository;
import fr.esgi.api.model.reservation.exceptions.ReservationNotFoundException;
import fr.esgi.api.model.reservation.place.Place;
import fr.esgi.api.model.reservation.place.PlaceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * JPA-based implementation of ReservationRepository.
 * Handles all persistence operations and mappings for reservations.
 */
@Service
public class ReservationRepositoryImpl implements ReservationRepository {
    private final ReservationJpaRepository reservationJpaRepository;
    private final EmployeeJpaRepository employeeJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final ReservationMapper reservationMapper;
    private final EmployeeMapper employeeMapper;
    private final PlaceMapper placeMapper;

    public ReservationRepositoryImpl(ReservationJpaRepository reservationJpaRepository, EmployeeJpaRepository employeeJpaRepository, PlaceJpaRepository placeJpaRepository, ReservationMapper reservationMapper, EmployeeMapper employeeMapper, PlaceMapper placeMapper) {
        this.reservationJpaRepository = reservationJpaRepository;
        this.employeeJpaRepository = employeeJpaRepository;
        this.placeJpaRepository = placeJpaRepository;
        this.reservationMapper = reservationMapper;
        this.employeeMapper = employeeMapper;
        this.placeMapper = placeMapper;
    }


    /**
     * Save a reservation to the database.
     * Maps domain to entities and returns the fully populated reservation.
     *
     * @param reservation domain Reservation
     * @return persisted Reservation with complete references
     */
    @Override
    public Reservation save(Reservation reservation) {
        EmployeeEntity employeeEntity = employeeJpaRepository.findById(reservation.getEmployee().getId()).orElseThrow(EmployeeNotFoundException::new);
        PlaceEntity placeEntity = placeJpaRepository.findById(reservation.getPlace().getId()).orElseThrow(PlaceNotFoundException::new);
        ReservationEntity entity = reservationMapper.toEntity(reservation, employeeEntity, placeEntity);

        ReservationEntity entitySaved = reservationJpaRepository.save(entity);
        return findById(entitySaved.getId());
    }

    /**
     * Save a list of reservations.
     *
     * @param reservations list of Reservation domain objects
     * @return list of persisted Reservations
     */
    @Override
    public List<Reservation> saveAll(List<Reservation> reservations) {
        return reservations.stream().map(this::save).toList();
    }

    /**
     * Check if a reservation exists for a place on a specific date.
     *
     * @param place the place to check
     * @param bookedFor the date to check
     * @return true if a reservation exists
     */
    @Override
    public boolean isExistByPlaceAndDate(Place place, LocalDate bookedFor) {
        return this.reservationJpaRepository.existsByPlace_IdAndBookedFor(place.getId(), bookedFor);
    }

    /**
     * Find a reservation by its ID.
     *
     * @param id UUID of the reservation
     * @return domain Reservation
     * @throws ReservationNotFoundException if not found
     */
    @Override
    public Reservation findById(UUID id) throws ReservationNotFoundException {
        ReservationEntity entity = this.reservationJpaRepository.findById(id).orElseThrow(ReservationNotFoundException::new);

        List<Reservation> reservations = mappedReservationsEntitiesToDomain(entity.getEmployee());
        Employee employee = employeeMapper.toDomain(entity.getEmployee(), reservations);
        Place place = placeMapper.toDomain(entity.getPlace());

        return reservationMapper.toDomain(entity, employee, place);
    }

    /**
     * Update an existing reservation.
     *
     * @param reservation domain Reservation with updated fields
     */
    @Override
    public void update(Reservation reservation) {
        EmployeeEntity employeeEntity = employeeJpaRepository.findById(reservation.getEmployee().getId()).orElseThrow(EmployeeNotFoundException::new);
        PlaceEntity placeEntity = placeJpaRepository.findById(reservation.getPlace().getId()).orElseThrow(PlaceNotFoundException::new);

        ReservationEntity entity = reservationMapper.toEntity(reservation, employeeEntity, placeEntity);
        entity.setId(reservation.getId());

        reservationJpaRepository.save(entity);
    }

    /**
     * Delete a reservation by its UUID.
     *
     * @param id UUID of the reservation to delete
     */
    @Override
    public void delete(UUID id) {
        reservationJpaRepository.deleteById(id);
    }

    /**
     * Deletes all EMPLOYEE reservations of the day that have not been checked in.
     * This is meant to run as a scheduled cleanup job.
     */
    @Override
    public void deleteNotCheckInReservation() {
        LocalDate now = LocalDate.now();
        String name = EmployeeRole.EMPLOYEE.name();
        reservationJpaRepository.deleteAllByCheckedInFalseAndBookedForAndEmployee_Role(now, name);
    }

    /**
     * Check if a reservation exists for a given employee on a specific date.
     *
     * @param employee the employee to check
     * @param date the date to check
     * @return true if a reservation exists
     */
    @Override
    public boolean isExistByEmployeeAndDate(Employee employee, LocalDate date) {
        EmployeeEntity employeeEntity = employeeJpaRepository.findById(employee.getId()).orElseThrow(EmployeeNotFoundException::new);
        return reservationJpaRepository.existsByEmployeeAndBookedFor(employeeEntity, date);
    }

    /**
     * Helper method to convert JPA reservation entities of an employee to domain reservations.
     *
     * @param entity EmployeeEntity containing reservations
     * @return list of domain Reservation objects
     */
    private List<Reservation> mappedReservationsEntitiesToDomain(EmployeeEntity entity) {
        List<ReservationEntity> reservations = entity.getReservations();
        return reservations.stream().map(reservation -> {
            Place place = placeMapper.toDomain(reservation.getPlace());
            return reservationMapper.toDomain(reservation, place);
        }).toList();
    }
}
