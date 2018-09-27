package gameElements;

/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */

/* This class represents a card in the game. Each card has a name,
 * which will be retrieved from the Names class, and a type. The type is represented
 * by a character: 's' for suspects, 'r' for rooms, and 'w' for weapons
 */
public class Card {
    private final String name;
    private final char type;

    public Card(String name, char type) {
        this.name = name;
        this.type = type;
    }

    public boolean hasName(String name) {
        return this.name.toLowerCase().equals(name.trim());
    }

    public String getName() {
        return this.name;
    }

    public char getType() {
        return this.type;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{").append(this.getType()).append(" - ").append(this.getName()).append(" - ").append(this.getType()).append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Card other = (Card) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type != other.type)
            return false;
        return true;
    }
}
