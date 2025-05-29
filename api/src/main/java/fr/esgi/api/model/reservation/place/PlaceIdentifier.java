package fr.esgi.api.model.reservation.place;

import java.util.Objects;

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
