package fr.esgi.api.model.reservation.exceptions;

public enum CannotBookExceptionMessage {
    MAXIMUM_POSSIBLE_RESERVATION_DAYS_EXCEED("Maximum number of bookings reached."),
    RESERVATION_ALREADY_EXITS("Reservation already exist. "),
    INVALID_DATE("Cannot book in the past. The date must be today or later."),
    INVALID_WORKING_DAYS("Cannot book in the past. The date must be today or later."),
    ;

    private final String message;

    CannotBookExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
