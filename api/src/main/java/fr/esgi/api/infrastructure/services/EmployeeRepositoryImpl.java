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

/**
 * JPA-based implementation of EmployeeRepository.
 * Handles mapping between EmployeeEntity and domain Employee.
 */
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

    /**
     * Fetch all employees from the database.
     *
     * @return list of all employees
     */
    @Override
    public List<Employee> getAll() {
        List<EmployeeEntity> employees = this.employeeJpaRepository.findAll();
        return employees.stream().map(employee -> {
            List<Reservation> reservations = mappedReservationsEntitiesToDomain(employee);
            return employeeMapper.toDomain(employee, reservations);
        }).toList();
    }

    /**
     * Retrieve an employee by ID.
     *
     * @param id employee UUID
     * @return the matching Employee
     * @throws EmployeeNotFoundException if none found
     */
    @Override
    public Employee getById(UUID id) throws EmployeeNotFoundException {
        EmployeeEntity entity = this.employeeJpaRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);

        List<Reservation> reservationList = mappedReservationsEntitiesToDomain(entity);
        return this.employeeMapper.toDomain(entity, reservationList);
    }

    /**
     * Save or update an employee record.
     *
     * @param employee domain Employee object
     * @return saved Employee with generated ID if new
     */
    @Override
    public Employee save(Employee employee) {
        EmployeeEntity entity = this.employeeMapper.toEntity(employee);
        entity = this.employeeJpaRepository.save(entity);
        return this.employeeMapper.toDomain(entity, mappedReservationsEntitiesToDomain(entity));
    }

    /**
     * Delete an employee by ID.
     *
     * @param id the UUID of the employee to delete
     * @throws EmployeeNotFoundException if the employee does not exist
     */
    @Override
    public void deleteById(UUID id) throws EmployeeNotFoundException {
        this.employeeJpaRepository.deleteById(id);
    }

    /**
     * Find an employee by email address.
     *
     * @param email address to look up
     * @return optional Employee if found
     */
    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeJpaRepository.findByEmail(email).map(entity -> employeeMapper.toDomain(entity, mappedReservationsEntitiesToDomain(entity)));
    }

    /**
     * Update an existing employee.
     *
     * @param employee the domain Employee object with updated values
     * @return the updated Employee object from the database
     * @throws EmployeeNotFoundException if the employee does not exist in the database
     */
    @Override
    public Employee update(Employee employee) throws EmployeeNotFoundException {
        EmployeeEntity updatedEntity = employeeMapper.toEntity(employee);
        updatedEntity.setId(employee.getId());
        EmployeeEntity saved = employeeJpaRepository.save(updatedEntity);
        List<Reservation> reservations = mappedReservationsEntitiesToDomain(saved);
        return employeeMapper.toDomain(saved, reservations);
    }

    /**
     * Convert JPA ReservationEntity list to domain Reservation list.
     *
     * @param entity EmployeeEntity containing reservations
     * @return list of Reservations or empty list
     */
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
