package fr.esgi.api.model.reservation.employee;

import fr.esgi.api.model.DomainException;

public class EmployeeNotFoundException extends DomainException {
    private static final String MESSAGE = "Employee not found.";
    public EmployeeNotFoundException() {
        super(MESSAGE);
    }
}
