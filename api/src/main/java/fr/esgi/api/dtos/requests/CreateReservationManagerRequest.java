package fr.esgi.api.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record CreateReservationManagerRequest(
        @NotNull Boolean electricalPlaceNeeded,
        @NotNull LocalDate bookedFor,
        @NotNull Integer daysNumber
) {
}
