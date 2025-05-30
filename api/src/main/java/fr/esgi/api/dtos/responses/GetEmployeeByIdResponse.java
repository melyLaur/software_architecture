package fr.esgi.api.dtos.responses;

import java.util.UUID;

public record GetEmployeeByIdResponse(
        UUID employeeId
) {
}
