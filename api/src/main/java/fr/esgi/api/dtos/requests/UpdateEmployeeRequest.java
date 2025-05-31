package fr.esgi.api.dtos.requests;

import fr.esgi.api.model.employee.EmployeeRole;
import jakarta.validation.constraints.*;

public record UpdateEmployeeRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull EmployeeRole role,
        @NotBlank String email
) {
    public UpdateEmployeeRequest {
        firstName = firstName.trim();
        lastName = lastName.trim();
        email = email.trim();
    }
}
