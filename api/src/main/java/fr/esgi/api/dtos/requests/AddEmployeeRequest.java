package fr.esgi.api.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record AddEmployeeRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String email
) {
    public AddEmployeeRequest {
        firstName = firstName.trim();
        lastName = lastName.trim();
        email = email.trim();
    }
}
