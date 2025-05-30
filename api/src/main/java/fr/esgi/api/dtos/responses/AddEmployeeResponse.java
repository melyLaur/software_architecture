package fr.esgi.api.dtos.responses;

import java.util.UUID;

public record AddEmployeeResponse(
        UUID employeeId,
        String firstName,
        String lastName,
        String email
) {
}
