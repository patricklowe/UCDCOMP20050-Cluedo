package gameElements;

import java.util.ArrayList;

/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */

public class Player {

    private final String name;
    private final Token token;
    private ArrayList<Card> deck = new ArrayList<Card>(); //The player's cards for the game
    private ArrayList<Card> seenCards = new ArrayList<Card>(); //Cards the player has been shown
    
    private boolean isActive = true; //is true until a player makes an incorrect accusation

    public Player(String name, Token token) {
        this.name = name;
        this.token = token;
    }

    public boolean hasName(String name) {
        return this.name.toLowerCase().equals(name.trim());
    }

    public String getName() {
        return name;
    }

    public Token getToken() {
        return token;
    }
    
    public void dealCard(Card c) {
    		this.deck.add(c);
    }
    
    public void seeCard(Card c) {
    		this.seenCards.add(c);
    }
    
    public ArrayList<Card> getCards(){
    		return this.deck;
    }
    
    public boolean isActive() {
    		return this.isActive;
    }
    
    public void setInactive() {
    		this.isActive = false; 
    }

    @Override
    public String toString() {
        return name + " (" + token.getName() + ")";
    }
    
    public String cardsToString() {
    		StringBuffer sb = new StringBuffer();
    	
    		for(Card c : deck) {
    			sb.append(c.toString() + "\n");
    		}
    		
    		for(Card c : seenCards) {
    			sb.append(c.toString() + "\n");
    		}
    		
    		return sb.toString();
    }
}
