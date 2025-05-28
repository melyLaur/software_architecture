package fr.esgi.api.dtos.responses;

public record ErrorResponse(
        String status,
        String message
) {
}
