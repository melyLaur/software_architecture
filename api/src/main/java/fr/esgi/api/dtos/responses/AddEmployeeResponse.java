package fr.esgi.api.dtos.responses;

import fr.esgi.api.model.employee.EmployeeRole;

import java.util.UUID;

public record AddEmployeeResponse(
        UUID employeeId,
        String firstName,
        String lastName,
        String email,
        EmployeeRole role
) {
}
