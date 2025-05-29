package fr.esgi.api.infrastructure.crons;

import fr.esgi.api.use_cases.reservation.DeleteNotCheckInReservations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteNotCheckInReservationsCronTest {
    @Mock
    private DeleteNotCheckInReservations deleteNotCheckInReservations;

    @InjectMocks
    private DeleteNotCheckInReservationsCron cron;

    @Test
    void should_delete_reservations_for_today_and_not_checked_in() {
        cron.process();

        verify(deleteNotCheckInReservations, times(1)).process();
        verifyNoMoreInteractions(deleteNotCheckInReservations);
    }

}