/*
 * Team ShrekBot
 *
 * Patrick Lowe 16725829
 * Aaron Cassidy 16349873
 * Yurii Demkiv 17207262
 */


import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import gameElements.Card;
import gameElements.Coordinates;
import gameElements.Deck;
import gameElements.Dice;
import gameElements.Map;
import gameElements.Names;
import gameElements.Player;
import gameElements.Players;
import gameElements.Room;
import gameElements.SolutionEnvelope;
import gameElements.Token;
import gameElements.Tokens;
import gameElements.Weapon;
import gameElements.Weapons;
import ui.UI;

public class Cluedo {

	private static final int MAX_NUM_PLAYERS = 6;
	static int turn = 0; 
	private final Tokens tokens = new Tokens();
	private final Players players = new Players();
	private final Dice dice = new Dice();
	private final Map map = new Map();
	private final Deck deck = new Deck();
	private final SolutionEnvelope solution = new SolutionEnvelope();
	private final Weapons weapons = new Weapons(map);
	private final UI ui = new UI(tokens, weapons);
	private final ArrayList<String> questionLog = new ArrayList<>();

	private void inputPlayerNames() {
		int numPlayersSoFar = 0;
		do {
			ui.inputName(players);

			if (!ui.inputIsDone()) {
				ui.inputToken(tokens);
				Token token = tokens.get(ui.getTokenName());
				players.add(new Player(ui.getPlayerName(), token));
				token.setOwned();
				numPlayersSoFar++;
			}
		} while (!ui.inputIsDone() && numPlayersSoFar < MAX_NUM_PLAYERS);
	}

	private void removeSolutionsFromDeck() {
		for (int i = 0; i < solution.size(); i++) {
			deck.removeSolution(solution.getCard(i));
		}
	}

	private void dealCardsToPlayers() {
		/* This gives the amount of cards to be dealt to each player
		 * by taking the integer part of the division of the deck size
		 * by the amount of players. Any cards not dealt under by this
		 * formula are left in the deck and are viewable to all players
		 */
		int deal = (int) (deck.size() / players.size());

		for (int i = 0; i < deal; i++) {
			for (Player p : players) {
				p.dealCard(deck.getTopCard());
				deck.dealCard();
			}
		}
	}

	private void takeTurns() {
		boolean moveOver, turnOver, gameOver = false, questionAsked;

		//Determine if the game should end by counting how many players cannot play
		int countActive = players.size();
		for(Player p : players) {
			if(!p.isActive()) {
				countActive--;
			}
		}

		do {
			if(countActive > 1) {
				turnOver = false;
				moveOver = false;
				questionAsked = false;
				if (players.getCurrentPlayer().isActive()) { //if a player has made an incorrect accusation they will not take any more turns
					do {
						Player currentPlayer = players.getCurrentPlayer();
						Token currentToken = currentPlayer.getToken();
						ui.inputCommand(currentPlayer);
						switch (ui.getCommand()) {
						case "roll": {
							if (!moveOver) {
								dice.roll();
								ui.displayDice(currentPlayer, dice);
								int squaresMoved = 0;
								if (currentToken.isInRoom()) {
									if (currentToken.getRoom().getNumberOfDoors() > 1) {
										boolean exitDone = false;
										do {
											ui.inputDoor(currentPlayer);
											if (ui.getDoor() >= 1 && ui.getDoor() <= currentToken.getRoom().getNumberOfDoors()) {
												currentToken.leaveRoom(ui.getDoor() - 1);
												exitDone = true;
											} else {
												ui.displayErrorNotADoor();
											}
										} while (!exitDone);
									} else {
										currentToken.leaveRoom();
									}
									ui.display();
								}
								do {
									ui.inputMove(currentPlayer, squaresMoved + 1, dice.getTotal());
									Coordinates currentPosition = currentToken.getPosition();
									Coordinates newPosition;
									if (map.isValidMove(currentPosition, ui.getMove())) {
										newPosition = map.getNewPosition(currentPosition, ui.getMove());
										if (map.isDoor(currentPosition, newPosition)) {
											Room room = map.getRoom(newPosition);
											currentToken.enterRoom(room);
										} else {
											currentToken.setPosition(newPosition);
										}
										squaresMoved++;
										if (squaresMoved == dice.getTotal() || currentPlayer.getToken().isInRoom()) {
											moveOver = true;
										}
										ui.display();
									} else {
										ui.displayErrorInvalidMove();
									}
								} while (!moveOver);
							} else {
								ui.displayErrorAlreadyMoved();
							}
							break;
						}
						case "passage": {
							if (!moveOver) {
								if (currentToken.isInRoom() && currentToken.getRoom().hasPassage()) {
									Room destination = currentToken.getRoom().getPassageDestination();
									currentToken.leaveRoom();
									currentToken.enterRoom(destination);
									// To show current player in new room via passage
									ui.display();
									if (JOptionPane.showConfirmDialog(null, "Would you like to ask a Question?") == JOptionPane.YES_OPTION) {
										if (!questionAsked) {
											questionAsked = questionPlayer();
										} else
											ui.displayString("You can't ask a question twice in one turn");
									} // End of accusation Question
									// Allow the player to make an accusation in the new room
									moveOver = true;
								} else {
									ui.displayErrorNoPassage();
								}
							} else {
								ui.displayErrorAlreadyMoved();
							}
							break;
						}
						case "log": {
							StringBuilder questionBuilder = new StringBuilder();
							for (String question :
								questionLog) {
								questionBuilder.append(question).append('\n');
							}

							JOptionPane.showMessageDialog(null, questionBuilder.toString());
							break;
						}
						case "help": {
							/* Shows a popup with the following information
							 * Will add more to info panel as accusations /
							 * suggestions are implemented
							 */
							JOptionPane.showMessageDialog(null, "\t\tHelp Panel:\n"
									+ "Typing 'Roll' will roll the dice\n"
									+ "Typing 'U' will move your token up 1 place\n"
									+ "Typing 'D' will move your token down 1 place\n"
									+ "Typing 'L' will move your token left 1 place\n"
									+ "Typing 'R' will move your token right 1 place\n"
									+ "Typing 'Passage' will allow you to enter a passage, if one is available\n"
									+ "Typing 'Clear' will clear the information panel of all text\n"
									+ "Typing 'Done' will end your turn\n"
									+ "Typing 'Quit' will end the game for all players\n"
									+ "Typing 'Question' in a room will allow you to suggest a solution\n"
									+ "\nFor Cards under Notes:\n"
									+ "X: Marks a card visible to you \n"
									+ "A: Marks a card visible to all players\n"
									+ "NO marks indicate that the card has not been used in an accusation\n"
									+ "\nWhatever you do, don't 'cheat'!\n");
							break;
						}
						case "cheat": {
							/*	Allows for the cheat command
							 *  This displays the Murderer, Weapon, and Room
							 */
							JOptionPane.showMessageDialog(null, solution.toString());
							break;
						}
						case "swamp": {
							play();
							break;
						}
						case "notes": {
							/* Used to display all available rooms / weapons / suspects
							 * Then lists the cards in players hand
							 * And the visible cards to all players (which will increase
							 * as the finished game plays out
							 */
							String allPlayers = "Suspects:\n";
							String allWeapons = "\nWeapons:\n";
							String allRooms = "\nRooms:\n";
							String playerCards = ""; //adding players cards first, then all visible cards as it is nicer formatting
							String allCards = "";

							/*	Returns a list of cards in current players deck
							 * 	Not added the X / A as this is much nicer formatting
							 */
							playerCards += currentPlayer.cardsToString();

							/* 	returns a list of all cards that were not dealt evenly 
							 * amongst players in future, when a suggestion is made I 
							 * guess that card will be added to the deck thus making it 
							 * visible using the below
							 */
							allCards += deck.toString() + "\n";

							// Adds all players names
							for (String s : Names.SUSPECT_NAMES) {
								if (playerCards.contains(s)) {
									allPlayers += s.toString() + " (X)" + "\n";
								} else if (allCards.contains(s)) {
									allPlayers += s.toString() + " (A)" + "\n";
								} else {
									allPlayers += s.toString() + "\n";
								}
							}

							for (String w : Names.WEAPON_NAMES) {
								if (playerCards.contains(w)) {
									allWeapons += w.toString() + " (X)" + "\n";
								} else if (allCards.contains(w)) {
									allWeapons += w.toString() + " (A)" + "\n";
								} else {
									allWeapons += w.toString() + "\n";
								}
							}

							/*	Returns all rooms on the map
							 *  Have prevent Cellar and Hall from being a Card
							 */
							for (String r : Names.ROOM_NAMES) {
								if (!r.equals("Cellar") && playerCards.contains(r)) {
									allRooms += r.toString() + " (X)" + "\n";
								} else if ((allCards.contains(r) && !r.equals("Cellar"))) {
									allRooms += r.toString() + " (A)" + "\n";
								} else if (!r.equals("Cellar")) {
									allRooms += r.toString() + "\n";
								}
							}

							JOptionPane.showMessageDialog(null, allPlayers + allWeapons + allRooms);
							break;
						}
						case "clear": {
							ui.clear();
							break;
						}
						case "done": {
							turnOver = true;
							break;
						}
						case "question": {
							if (!questionAsked) {
								questionAsked = questionPlayer();
							} else
								ui.displayString("You can't ask a question twice in one turn");
							break;
						}
						case "accuse": {
							if (currentToken.isInRoom()) {
								accusePlayer();
							} else {
								ui.displayString("You cannot make an accusation if you are not in the Cellar");
							}
							break;
						}
						}
					} while (!turnOver);
				} else {
					for(Player p : players) {
						if(p.isActive()) {
							JOptionPane.showMessageDialog(null, 
									p.getName() + " is the last active player\n" + 
											p.getName() + " wins!");
							gameOver = true;
							turnOver = true; 
						}
					}
				}
			}
			players.turnOver();
			ui.clear();
		} while (!gameOver);
	}

	private boolean questionPlayer() {
		Player currentPlayer = players.getCurrentPlayer();
		Room room = currentPlayer.getToken().getRoom();

		JTextField weaponText = new JTextField();
		JTextField suspectText = new JTextField();

		Object[] accusationFields = {
				"Weapon: ", weaponText,
				"Suspect: ", suspectText
		};

		if (JOptionPane.showConfirmDialog(null, accusationFields, "Please provide accusation information", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			String accusationSuspect = suspectText.getText().toLowerCase().trim();
			String accusationWeapon = weaponText.getText().toLowerCase().trim();
			Weapon suspectWeapon = null;
			Token suspectPlayerToken = null;
			for (String s : Names.SUSPECT_NAMES) {
				if (accusationSuspect.equals(s.toLowerCase().trim())) {
					suspectPlayerToken = tokens.get(s);
				}
			}
			for (String s : Names.WEAPON_NAMES) {
				if (accusationWeapon.equals(s.toLowerCase().trim())) {
					suspectWeapon = weapons.get(s);
				}
			}

			if (suspectText.getText().isEmpty() || weaponText.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Empty Accusation");
			} else if (suspectPlayerToken == null || suspectWeapon == null) {
				JOptionPane.showMessageDialog(null, "No such weapon or token");
			} else {
				questionLog.add(currentPlayer.getName() + " asked if " + suspectText.getText() + " used "
						+ weaponText.getText() + " in the " + room.toString());

				/*
				 * Compares all suspects names and then assigns the suspectPlayer
				 * and suspectToken as the current Players accusation, gets them
				 * ready to be moved
				 */

				suspectPlayerToken.enterRoom(room);
				suspectWeapon.enterRoom(room);
				ui.display();
				questionResponses(accusationSuspect, room.toString(), accusationWeapon);
				return true;
			} // End of Confirm Suspect & Weapon pop-up

		} // End of valid accusation entry
		else {
			JOptionPane.showMessageDialog(null, "Your accusation has been cancelled.");
		}
		return false;
	} // End of Accusation

	private void questionResponses(String suspectName, String roomName, String weaponName) {
		Stack<Player> respondingPlayers = new Stack<Player>();
		boolean responseGiven = false;

		for(Player p : players) { //add questioned player to response list first
			if(p.getToken().getName().toLowerCase().trim().equals(suspectName)) {
				respondingPlayers.push(p);
			}
		}

		for(Player p : players) { //add other players not including questioning player or if questioned token is not owned
			if(!p.equals(players.getCurrentPlayer()) && !p.getToken().getName().toLowerCase().trim().equals(suspectName)) {
				respondingPlayers.push(p);
			}
		}

		Collections.reverse(respondingPlayers);

		do {
			ArrayList<Card> matchingCards = new ArrayList<Card>();
			String matches = "";
			String response;
			boolean responseValid = false;
			Player current = respondingPlayers.pop();
			ArrayList<Card> cards = current.getCards();

			JOptionPane.showConfirmDialog(null, "Is " + current.getToken().getName() + " at the screen?", "Questioning", JOptionPane.OK_OPTION);

			ui.clear();
			ui.displayString("Questioning " + current.getName() + "(" + current.getToken().getName() + ")");

			for(Card c : cards) {
				if(c.getName().toLowerCase().trim().equals(suspectName.toLowerCase().trim())
						|| c.getName().toLowerCase().trim().equals(roomName.toLowerCase().trim())
						|| c.getName().toLowerCase().trim().equals(weaponName.toLowerCase().trim())) {
					matchingCards.add(c);
				}
			}

			for(Card c : matchingCards) {
				matches += "\t-" + c.getName() + "\n";
			}

			ui.displayString("Your matching cards are:\n" + matches + "Enter the name of a card to show it, or done if you have no matches");

			do {
				response = JOptionPane.showInputDialog("Enter a response: ");
				if(response.toLowerCase().trim().equals("done")) {
					responseValid = true;
					responseGiven = true;
				} 
				else {
					for(Card c : matchingCards) {
						if(response.toLowerCase().trim().equals(c.getName().toLowerCase().trim())) {
							players.getCurrentPlayer().seeCard(c);
							responseValid = true;
							responseGiven = true;
						} 
						else if(!response.toLowerCase().trim().equals(c.getName().toLowerCase().trim())) {
							ui.displayString("You do not have that card.");
							responseValid = false;
							responseGiven = false;
						}
					}
				}
			} while(!responseValid);
		} while(respondingPlayers.size() > 0 && !responseGiven);
	}

	/*
	 * This function handles the final accusation
	 */
	private void accusePlayer() {
		Player currentPlayer = players.getCurrentPlayer();
		Room currentRoom = currentPlayer.getToken().getRoom();

		JTextField suspectTextField = new JTextField();
		JTextField locationTextField = new JTextField();
		JTextField weaponTextField = new JTextField();

		Object[] accusationFields = {
				"Suspect: ", suspectTextField,
				"Crime Scene: ", locationTextField,
				"Weapon: ", weaponTextField
		};

		if (!(currentRoom.hasName("Cellar"))) {
			ui.displayString("You cannot make an accusation if you are not in the Cellar");
		} else {
			JOptionPane.showConfirmDialog(null, accusationFields, "Enter your accusation",
					JOptionPane.OK_CANCEL_OPTION);

			if (suspectTextField.getText().isEmpty() || locationTextField.getText().isEmpty()
					|| weaponTextField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "You must fill all fields!");
			} else {
				String suspect = suspectTextField.getText().toLowerCase().trim();
				String location = locationTextField.getText().toLowerCase().trim();
				String weapon = weaponTextField.getText().toLowerCase().trim();

				if (suspect.equals(solution.getCard(0).getName().toLowerCase())
						&& location.equals(solution.getCard(1).getName().toLowerCase())
						&& weapon.equals(solution.getCard(2).getName().toLowerCase())) {
					JOptionPane.showMessageDialog(null, "You are right!");
					System.exit(0);
				} else {
					JOptionPane.showMessageDialog(null
							, currentPlayer.getName() + " has made an incorrect accusation and is now inactive!");
					currentPlayer.setInactive();
				}
			}
		}
	}

	private void makePlayerOrder() {
		ui.clear();
		ui.displayString("Everybody needs to roll. Whoever rolls the most - goes first!");
		// First round of rolls. Used LinkedList, because deleting elements is less complex than in ArrayList
		LinkedList<Player> firstPlayers = getBiggestRolls(players.getAllPlayers());
		// If some of the players rolled the same biggest amount, they have another round
		while (firstPlayers.size() > 1) {
			ui.displayString("Some of the rolls matched. Second round!");
			firstPlayers = getBiggestRolls(firstPlayers);
		}
		// Remove the winner from his current position
		players.remove(firstPlayers.getFirst());
		// Add him to the first position
		players.add(0, firstPlayers.getFirst());
		ui.displayString(firstPlayers.getFirst().getToken().getName() + " takes the first turn!");
	}

	// Made another method for recursion if necessary
	private LinkedList<Player> getBiggestRolls(List<Player> players) {
		// An array for holding the roll winners
		LinkedList<Player> firstPlayers = new LinkedList<>();
		boolean rollOver;
		int biggestRoll = 0;

		for (Player player : players) {
			ui.displayString(player.getName() + " roll the dice");
			rollOver = false;
			do {
				ui.inputCommand(player);
				if (ui.getCommand().equals("roll")) {
					dice.roll();
					ui.displayDice(player, dice);
					if (dice.getTotal() > biggestRoll) {
						// If we get the new biggest roll, the list is cleared
						firstPlayers.clear();
						firstPlayers.add(player);
						biggestRoll = dice.getTotal();
					} else if (dice.getTotal() == biggestRoll)
						// If we get same amount of the roll, the list gets more players
						firstPlayers.add(player);
					rollOver = true;
				} else
					ui.displayString("Write 'roll' to roll the dice");
			} while (!rollOver);
		}

		return firstPlayers;
	}

	public void play() {
		/*
		 * Loads the shrek remix
		 */
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("./swamp.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		/*
		 * Creates a GIF for Shrek
		 */
		URL url = this.getClass().getResource("shrek.gif");
		Icon icon = new ImageIcon(url);
		JLabel label = new JLabel(icon);
		JFrame f = new JFrame("WHAT. ARE YOU DOING. IN MY SWAMP?!");
		f.getContentPane().add(label);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public static void main(String[] args) {
		Cluedo game = new Cluedo();
		game.removeSolutionsFromDeck();
		game.inputPlayerNames();
		game.makePlayerOrder();
		game.dealCardsToPlayers();
		game.takeTurns();
		System.exit(0);
	}

}
