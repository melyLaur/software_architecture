package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.dtos.responses.GetReservationResponse;
import fr.esgi.api.model.DomainException;
import fr.esgi.api.model.reservation.Reservation;
import fr.esgi.api.model.reservation.ReservationRepository;
import fr.esgi.api.model.reservation.place.PlaceType;
import fr.esgi.api.presentation.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for retrieving a single reservation by its ID.
 */
@Service
public class GetReservation {

    private final ReservationRepository reservationRepository;

    public GetReservation(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Retrieve a reservation and map it to a response DTO.
     *
     * @param id the UUID of the reservation
     * @return the corresponding reservation response
     * @throws ApiException with 404 status if the reservation is not found
     */
    public GetReservationResponse process(UUID id) {
        try {
            Reservation reservation = reservationRepository.findById(id);

            boolean isElectric = reservation.getPlace().getType() == PlaceType.ELECTRICAL;

            return new GetReservationResponse(
                    reservation.getId(),
                    reservation.getBookedFor(),
                    reservation.getPlace().getIdentifier().toString(),
                    isElectric
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
