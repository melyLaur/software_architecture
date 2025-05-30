package fr.esgi.api.dtos.requests;

import fr.esgi.api.model.employee.EmployeeRole;
import jakarta.validation.constraints.*;

public record AddEmployeeRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String email,
        @NotNull EmployeeRole role
) {
    public AddEmployeeRequest {
        firstName = firstName.trim();
        lastName = lastName.trim();
        email = email.trim();
    }
}
