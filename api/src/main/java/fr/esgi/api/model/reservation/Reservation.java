package fr.esgi.api.model.reservation;

import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.reservation.place.Place;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a reservation made by an employee for a parking place.
 * Encapsulates reservation details such as the employee, place, date, and check-in status.
 */
public class Reservation {
    private UUID id;
    private Employee employee;
    private Place place;
    private LocalDate bookedFor;
    private boolean isCheckedIn;

    public Reservation(UUID id, Employee employee, Place place, LocalDate startDate, boolean isCheckedIn) {
        this.id = id;
        this.employee = employee;
        this.place = place;
        this.bookedFor = startDate;
        this.isCheckedIn = isCheckedIn;
    }

    public Reservation(UUID id, Place place, LocalDate startDate, boolean isCheckedIn) {
        this.id = id;
        this.place = place;
        this.bookedFor = startDate;
        this.isCheckedIn = isCheckedIn;
    }

    public static Reservation create(Employee employee, Place place, LocalDate bookedFor) {
        return new Reservation(UUID.randomUUID(), employee, place, bookedFor, false);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        isCheckedIn = checkedIn;
    }

    public LocalDate getBookedFor() {
        return bookedFor;
    }

    public void setBookedFor(LocalDate bookedFor) {
        this.bookedFor = bookedFor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
