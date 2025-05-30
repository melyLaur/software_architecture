package fr.esgi.api.model.reservation.employee;

import fr.esgi.api.model.DomainException;

public class EmployeeNotFoundException extends DomainException {
    private static final String MESSAGE = "L'employé n'existe pas.";
    public EmployeeNotFoundException() {
        super(MESSAGE);
    }
}
