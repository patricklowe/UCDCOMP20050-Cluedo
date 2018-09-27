package gameElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */

/* This class represents the deck of cards used in the game.
 */

public class Deck implements Iterable<Card>, Iterator<Card> {
    private ArrayList<Card> cards = new ArrayList<Card>();
    private Iterator<Card> iterator;

    public Deck() {
        for (String s : Names.ROOM_NAMES) {
            /*	The cellar is just for accusations, and murder cannot
			 * be committed in the hall, they're for the map
			 */
            if (!s.equals("Cellar")) {
                cards.add(new Card(s, 'R'));
            }
        }

        for (String s : Names.SUSPECT_NAMES) {
            cards.add(new Card(s, 'S'));
        }

        for (String s : Names.WEAPON_NAMES) {
            cards.add(new Card(s, 'W'));
        }

        Collections.shuffle(cards);
    }

    /* This method is used for removing the solution cards from the deck.
     * The method dealCard is used for giving cards to players
     */
    public void removeSolution(Card c) {
        for (Iterator<Card> it = cards.iterator(); it.hasNext(); ) {
            Card d = it.next();
            if (c.getName().equals(d.getName())) {
                it.remove();
            }
        }
    }

    /* This method is used for dealing cards to players.
     * It simply removes the last object in the arraylist cards
     */
    public void dealCard() {
        if (!cards.isEmpty())
            this.cards.remove(cards.size() - 1);
    }

    public Card getTopCard() {
        return this.cards.get(cards.size() - 1);
    }

    public int size() {
        return this.cards.size();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        if (this.cards.isEmpty()) {
            sb.append("All cards have been dealt.");
        } else {
            for (Card c : cards) {
                sb.append(c.toString() + "\n");
            }
        }
        return sb.toString();
    }

    @Override
    public Iterator<Card> iterator() {
        iterator = cards.iterator();
        return iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Card next() {
        return iterator.next();
    }
}
