package fr.esgi.api.model.reservation;

import fr.esgi.api.model.employee.Employee;
import fr.esgi.api.model.reservation.place.Place;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Reservation {
    private UUID id;
    private Employee employee;
    private Place place;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCheckedIn;

    public Reservation(UUID id, Employee employee, Place place, LocalDate startDate, LocalDate endDate, boolean isCheckedIn) {
        this.id = id;
        this.employee = employee;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCheckedIn = isCheckedIn;
    }

    public Reservation(UUID id, Place place, LocalDate startDate, LocalDate endDate, boolean isCheckedIn) {
        this.id = id;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCheckedIn = isCheckedIn;
    }

    public static Reservation createFromEmployee(Employee employee, Place place, LocalDate bookedFor) {
        return new Reservation(UUID.randomUUID(), employee, place, bookedFor, bookedFor, false);
    }

    public static Reservation createForManager(Employee employee, Place place, LocalDate start) {
        LocalDate end = start.plusDays(29);
        return new Reservation(UUID.randomUUID(), employee, place, start, end, false);
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        isCheckedIn = checkedIn;
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
