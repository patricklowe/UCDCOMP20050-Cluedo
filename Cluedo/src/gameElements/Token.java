package gameElements;

/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */

import java.awt.*;

public class Token {

    private final String name;
    private final Color color;
    private Coordinates position;
    private boolean isInRoom = false, isOwned = false;
    private Room room;

    Token(String name, Color color, Coordinates position) {
        this.name = name;
        this.color = color;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public boolean hasName(String name) {
        return this.name.toLowerCase().equals(name.toLowerCase().trim());
    }

    public Color getColor() {
        return color;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public Coordinates getPosition() {
        return position;
    }

    public void enterRoom(Room room) {
        this.room = room;
        position = this.room.addItem();
        isInRoom = true;
    }

    public void leaveRoom(int doorIndex) {
        room.removeItem(position);
        position = room.getDoorCoordinates(doorIndex);
        isInRoom = false;
    }

    public void leaveRoom() {
        room.removeItem(position);
        position = room.getDoorCoordinates(0);
        isInRoom = false;
    }

    public Room getRoom() {
        return room;
    }

    public boolean isInRoom() {
        return isInRoom;
    }

    public void setOwned() {
        isOwned = true;
    }

    public boolean isOwned() {
        return isOwned;
    }
}