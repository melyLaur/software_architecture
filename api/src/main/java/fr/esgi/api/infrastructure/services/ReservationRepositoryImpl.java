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
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.reservation.ReservationRepository;
import fr.esgi.api.model.reservation.employee.Employee;
import fr.esgi.api.model.reservation.employee.EmployeeNotFoundException;
import fr.esgi.api.model.reservation.employee.EmployeeRole;
import fr.esgi.api.model.reservation.exceptions.ReservationNotFoundException;
import fr.esgi.api.model.reservation.place.Place;
import fr.esgi.api.model.reservation.place.PlaceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    @Override
    public Reservation save(Reservation reservation) {
        EmployeeEntity employeeEntity = employeeJpaRepository.findById(reservation.getEmployee().getId()).orElseThrow(EmployeeNotFoundException::new);
        PlaceEntity placeEntity = placeJpaRepository.findById(reservation.getPlace().getId()).orElseThrow(PlaceNotFoundException::new);
        ReservationEntity entity = reservationMapper.toEntity(reservation, employeeEntity, placeEntity);

        ReservationEntity entitySaved = reservationJpaRepository.save(entity);
        return findById(entitySaved.getId());
    }

    @Override
    public boolean isExistByPlaceAndDate(Place place, LocalDate bookedFor) {
        return this.reservationJpaRepository.existsByPlace_IdAndBookedFor(place.getId(), bookedFor);
    }

    @Override
    public Reservation findById(UUID id) throws ReservationNotFoundException {
        ReservationEntity entity = this.reservationJpaRepository.findById(id).orElseThrow(ReservationNotFoundException::new);

        List<Reservation> reservations = mappedReservationsEntitiesToDomain(entity.getEmployee());
        Employee employee = employeeMapper.toDomain(entity.getEmployee(), reservations);
        Place place = placeMapper.toDomain(entity.getPlace());

        return reservationMapper.toDomain(entity, employee, place);
    }

    @Override
    public void update(Reservation reservation) {
        EmployeeEntity employeeEntity = employeeJpaRepository.findById(reservation.getEmployee().getId()).orElseThrow(EmployeeNotFoundException::new);
        PlaceEntity placeEntity = placeJpaRepository.findById(reservation.getPlace().getId()).orElseThrow(PlaceNotFoundException::new);

        ReservationEntity entity = reservationMapper.toEntity(reservation, employeeEntity, placeEntity);
        entity.setId(reservation.getId());

        reservationJpaRepository.save(entity);
    }

    @Override
    public void delete(UUID id) {
        reservationJpaRepository.deleteById(id);
    }

    @Override
    public void deleteNotCheckInReservation() {
        LocalDate now = LocalDate.now();
        String name = EmployeeRole.EMPLOYEE.name();
        reservationJpaRepository.deleteAllByCheckedInFalseAndBookedForAndEmployee_Role(now, name);
    }

    private List<Reservation> mappedReservationsEntitiesToDomain(EmployeeEntity entity) {
        List<ReservationEntity> reservations = entity.getReservations();
        return reservations.stream().map(reservation -> {
            Place place = placeMapper.toDomain(reservation.getPlace());
            return reservationMapper.toDomain(reservation, place);
        }).toList();
    }
}
