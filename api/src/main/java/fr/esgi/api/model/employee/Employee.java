package fr.esgi.api.model.employee;

import fr.esgi.api.dtos.requests.AddEmployeeRequest;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.employee.email.Email;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Employee {
    private UUID id;
    private String lastName;
    private String firstName;
    private EmployeeRole role;
    private List<Reservation> reservations;
    private Email email;

    public Employee(UUID id, String lastName, String firstName, EmployeeRole role, List<Reservation> reservations, Email email) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.reservations = reservations;
        this.email = email;
    }

    private Employee(String lastName, String firstName, EmployeeRole role, List<Reservation> reservations, Email email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.reservations = reservations;
        this.email = email;
    }

    public void update(String lastName, String firstName, EmployeeRole role, Email email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.email = email;
    }

    public static Employee create(AddEmployeeRequest addEmployeeRequest) {
        return new Employee(
                addEmployeeRequest.lastName(),
                addEmployeeRequest.firstName(),
                addEmployeeRequest.role(),
                Collections.emptyList(),
                Email.of(addEmployeeRequest.email())
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
