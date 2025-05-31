package fr.esgi.api.model.reservation.place;

import java.util.Objects;

/**
 * Represents the physical identifier of a parking place.
 * A place is uniquely defined by its row (e.g., 'A') and number (e.g., 1).
 */
public class PlaceIdentifier {
    private char row;
    private short number;

    private PlaceIdentifier(char row, short number) {
        this.row = row;
        this.number = number;
    }

    public static PlaceIdentifier of(char row, short number) {
        return new PlaceIdentifier(row, number);
    }

    public char getRow() {
        return row;
    }

    public void setRow(char row) {
        this.row = row;
    }

    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlaceIdentifier that = (PlaceIdentifier) o;
        return row == that.row && number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }

    @Override
    public String toString() {
        return row + String.valueOf(number);
    }
}
