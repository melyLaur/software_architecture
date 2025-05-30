package fr.esgi.api.model.reservation.employee;

import fr.esgi.api.model.DomainException;

public class EmployeeAlreadyExist extends DomainException {
  private static final String MESSAGE = "Un employé existe déjà avec cet email.";
  public EmployeeAlreadyExist() {
    super(MESSAGE);
  }
}
