package gameElements;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */

public class Players implements Iterable<Player>, Iterator<Player> {

    private final ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerIndex;
    private int suspectPlayerIndex;
    private Iterator<Player> iterator;

    public void add(Player player) {
        players.add(player);
    }

    public ArrayList<Player> getAllPlayers() {
        return players;
    }

    public boolean contains(String name) {
        for (Player player : players) {
            if (player.hasName(name)) {
                return true;
            }
        }
        return false;
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public void add(int index, Player player) {
        players.add(index, player);
    }

    public int size() {
        return players.size();
    }

    public Player get(int index) {
        return players.get(index);
    }

    public void setCurrentPlayer(String name) {
        currentPlayerIndex = 0;
        while (players.get(currentPlayerIndex).hasName(name)) {
            currentPlayerIndex++;
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void turnOver() {
        if (currentPlayerIndex < players.size() - 1) {
            currentPlayerIndex++;
        } else {
            currentPlayerIndex = 0;
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Player next() {
        return iterator.next();
    }

    @Override
    public Iterator<Player> iterator() {
        iterator = players.iterator();
        return iterator;
    }

}
