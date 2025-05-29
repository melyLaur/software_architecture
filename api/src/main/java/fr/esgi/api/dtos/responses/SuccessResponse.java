package fr.esgi.api.dtos.responses;

public record SuccessResponse(
        boolean success,
        Object data
) {
}
