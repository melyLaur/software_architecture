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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Employee save(Employee employee) {
        EmployeeEntity entity = this.employeeMapper.toEntity(employee);
        entity = this.employeeJpaRepository.save(entity);
        return this.employeeMapper.toDomain(entity, mappedReservationsEntitiesToDomain(entity));
    }

    @Override
    public void deleteById(UUID id) throws EmployeeNotFoundException {
        this.employeeJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeJpaRepository.findByEmail(email).map(entity -> employeeMapper.toDomain(entity, mappedReservationsEntitiesToDomain(entity)));
    }

    @Override
    public Employee update(Employee employee) throws EmployeeNotFoundException {
        EmployeeEntity updatedEntity = employeeMapper.toEntity(employee);
        updatedEntity.setId(employee.getId());
        EmployeeEntity saved = employeeJpaRepository.save(updatedEntity);
        List<Reservation> reservations = mappedReservationsEntitiesToDomain(saved);
        return employeeMapper.toDomain(saved, reservations);
    }

    private List<Reservation> mappedReservationsEntitiesToDomain(EmployeeEntity entity) {
        List<ReservationEntity> reservations = entity.getReservations();
        if (reservations == null) {
            return Collections.emptyList();
        }
        return reservations.stream().map(reservation -> {
            Place place = placeMapper.toDomain(reservation.getPlace());
            return reservationMapper.toDomain(reservation, place);
        }).toList();
    }
}
