package gameElements;
/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */

public class Weapon {

    private final String name;
    private Coordinates position;
    private Room room;

    Weapon(String name, Room room) {
        this.name = name;
        position = room.addItem();
    }

    public String getName() {
        return name;
    }

    public Coordinates getPosition() {
        return position;
    }

    public void enterRoom(Room room) {
        this.room = room;
        position = this.room.addItem();
    }

    public boolean hasName(String name) {
        return this.name.toLowerCase().equals(name.toLowerCase().trim());
    }

}
