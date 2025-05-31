package fr.esgi.api.model.reservation;

import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.reservation.exceptions.ReservationNotFoundException;
import fr.esgi.api.model.reservation.place.Place;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository abstraction for Reservation persistence operations.
 */
public interface ReservationRepository {
    /**
     * Saves a single reservation to the database.
     *
     * @param reservation the reservation to persist
     * @return the saved reservation with its generated ID (if applicable)
     */
    Reservation save(Reservation reservation);

    /**
     * Saves a batch of reservations.
     *
     * @param reservations list of reservations to persist
     * @return list of saved reservations
     */
    List<Reservation> saveAll(List<Reservation> reservations);

    /**
     * Checks whether a place is already reserved on a given date.
     *
     * @param place      the place to check
     * @param bookedFor  the target date
     * @return true if a reservation already exists, false otherwise
     */
    boolean isExistByPlaceAndDate(Place place, LocalDate bookedFor);

    /**
     * Retrieves a reservation by its unique identifier.
     *
     * @param id the UUID of the reservation
     * @return the matching reservation
     * @throws ReservationNotFoundException if no reservation is found
     */
    Reservation findById(UUID id) throws ReservationNotFoundException;

    /**
     * Updates an existing reservation.
     * The reservation must already exist in the database.
     *
     * @param reservation the reservation with updated data
     */
    void update(Reservation reservation);

    /**
     * Deletes a reservation by its unique identifier.
     *
     * @param id the ID of the reservation to delete
     */
    void delete(UUID id);

    /**
     * Deletes all reservations where the employee did not check in
     * and the booking date matches the current date,
     * but only for employees with the EMPLOYEE role.
     */
    void deleteNotCheckInReservation();

    /**
     * Checks whether a given employee has already reserved a place for a specific date.
     *
     * @param employee   the employee to check
     * @param date       the reservation date
     * @return true if a reservation exists, false otherwise
     */
    boolean isExistByEmployeeAndDate(Employee employee, LocalDate date);
}
