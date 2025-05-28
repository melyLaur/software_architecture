package fr.esgi.api.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

public record GetReservationResponse(
        UUID reservationId,
        LocalDate date,
        String parkingNumber,
        boolean isElectric
) {
}
