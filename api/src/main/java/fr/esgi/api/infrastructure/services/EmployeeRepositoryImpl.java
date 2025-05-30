package fr.esgi.api.infrastructure.services;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import fr.esgi.api.infrastructure.jpa.entities.ReservationEntity;
import fr.esgi.api.infrastructure.jpa.repositories.EmployeeJpaRepository;
import fr.esgi.api.infrastructure.mappers.EmployeeMapper;
import fr.esgi.api.infrastructure.mappers.PlaceMapper;
import fr.esgi.api.infrastructure.mappers.ReservationMapper;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.employee.EmployeeNotFoundException;
import fr.esgi.api.model.employee.EmployeeRepository;
import fr.esgi.api.model.reservation.place.Place;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final EmployeeJpaRepository employeeJpaRepository;
    private final EmployeeMapper employeeMapper;
    private final ReservationMapper reservationMapper;
    private final PlaceMapper placeMapper;

    public EmployeeRepositoryImpl(EmployeeJpaRepository employeeJpaRepository, EmployeeMapper employeeMapper, ReservationMapper reservationMapper, PlaceMapper placeMapper) {
        this.employeeJpaRepository = employeeJpaRepository;
        this.employeeMapper = employeeMapper;
        this.reservationMapper = reservationMapper;
        this.placeMapper = placeMapper;
    }

    @Override
    public List<Employee> getAll() {
        List<EmployeeEntity> employees = this.employeeJpaRepository.findAll();
        return employees.stream().map(employee -> {
            List<Reservation> reservations = mappedReservationsEntitiesToDomain(employee);
            return employeeMapper.toDomain(employee, reservations);
        }).toList();
    }

    @Override
    public Employee getById(UUID id) throws EmployeeNotFoundException {
        EmployeeEntity entity = this.employeeJpaRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);

        List<Reservation> reservationList = mappedReservationsEntitiesToDomain(entity);
        return this.employeeMapper.toDomain(entity, reservationList);
    }

    private List<Reservation> mappedReservationsEntitiesToDomain(EmployeeEntity entity) {
        List<ReservationEntity> reservations = entity.getReservations();
        return reservations.stream().map(reservation -> {
            Place place = placeMapper.toDomain(reservation.getPlace());
            return reservationMapper.toDomain(reservation, place);
        }).toList();
    }
}
