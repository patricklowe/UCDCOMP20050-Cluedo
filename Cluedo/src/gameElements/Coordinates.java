package gameElements;

/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */

public class Coordinates {

    private int col, row;

    Coordinates(int col, int row) {
        this.col = col;
        this.row = row;
    }

    Coordinates(Coordinates coordinates) {
        col = coordinates.getCol();
        row = coordinates.getRow();
    }

    public void add(Coordinates coordinates) {
        col = col + coordinates.getCol();
        row = row + coordinates.getRow();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
        return "(" + col + "," + row + ")";
    }
}
