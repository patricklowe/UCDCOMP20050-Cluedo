package gameElements;

import java.util.ArrayList;
import java.util.Random;

/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */

/* This class represents the solution to the game. 
 * 3 random numbers are generated, each corresponding to a position in the arrays
 * of the Name class. From these, the solution to the game is constructed. 
 * The cards which are created here are then checked against the deck for the game.
 * The cards which match are removed from the deck so they cannot be dealt to players. 
 */

public class SolutionEnvelope {
    private final ArrayList<Card> solution = new ArrayList<Card>();

    private int minIndex = 0, maxSuspectAndWeaponIndex = 5, maxRoomIndex = 8;
    private Random random = new Random();
    private final int culprit = random.nextInt(maxSuspectAndWeaponIndex - minIndex + 1) + minIndex;
    private final int location = random.nextInt(maxRoomIndex - minIndex + 1) + minIndex;
    private final int murderWeapon = random.nextInt(maxSuspectAndWeaponIndex - minIndex + 1) + minIndex;

    public SolutionEnvelope() {
        solution.add(new Card(Names.SUSPECT_NAMES[culprit], 's'));
        solution.add(new Card(Names.ROOM_NAMES[location], 'r'));
        solution.add(new Card(Names.WEAPON_NAMES[murderWeapon], 'w'));
    }

    public Card getCard(int i) {
        return this.solution.get(i);
    }

    public int size() {
        return this.solution.size();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("The murder was committed by ").append(this.getCard(0).getName()).append(" in the ")
                .append(this.getCard(1).getName()).append(" with the ")
                .append(this.getCard(2).getName());

        return sb.toString();
    }
}
