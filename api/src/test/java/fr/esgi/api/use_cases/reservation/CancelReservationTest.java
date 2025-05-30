package fr.esgi.api.use_cases.reservation;

import fr.esgi.api.model.reservation.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelReservationTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private CancelReservation cancelReservation;

    @Test
    void should_call_delete() {
        UUID id = UUID.randomUUID();
        cancelReservation.process(id);
        verify(reservationRepository, times(1)).delete(id);

        verifyNoMoreInteractions(reservationRepository);
    }
}