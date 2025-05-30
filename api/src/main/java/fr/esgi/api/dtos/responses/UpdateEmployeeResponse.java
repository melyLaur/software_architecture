package fr.esgi.api.dtos.responses;

import fr.esgi.api.model.employee.EmployeeRole;

import java.util.UUID;

public record UpdateEmployeeResponse(
        UUID employeeId,
        String firstName,
        String lastName,
        EmployeeRole role,
        String email
) {
}
