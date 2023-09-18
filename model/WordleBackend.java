package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 * The model for the Wordle game. Keeps track of the account, who is logged in,
 * the current puzzle, and other aspects necessary to run the game. This also
 * runs the logic behind the Wordle game (for example, receiving messages from
 * Puzzles).
 * 
 * @author Amelia Matheson and Aspen Cross
 * @since April 9, 2023
 */
public class WordleBackend extends WordleObservable{
	private Puzzle puzzle;
	private PlayerAccount currPlayer;
	private HashMap<String, PlayerAccount> accountDictionary;
	private ArrayList<PlayerAccount> leaderboard;
	private boolean gameOver;
	private boolean won;
	

	public WordleBackend() {
		currPlayer = null;
		accountDictionary = new HashMap<String, PlayerAccount>();
		leaderboard = new ArrayList<>();
		gameOver = false;
		won = false;
		
	}

	/**
	 * Logs player in
	 * 
	 * @param username A String representing player's username
	 * 
	 * @param password A String representing player's password 
	 * 
	 * @return boolean indicating whether or not player is successfully logged in
	 */
	public boolean login(String username, String password) {
		// check if username is in the dictionary
		if (accountDictionary.containsKey(username)) {
			String pass = accountDictionary.get(username).getPassword();
			if (password.equals(pass)) {
				currPlayer = accountDictionary.get(username);
				
				puzzle = currPlayer.getPuzzle();
				notifyObservers(this);
				return true;
			}
		}
		// username not registered
		return false;
	}

	/**
	 * Create new player account and logs them in
	 * 
	 * @param username A String representing player's username
	 * 
	 * @param password A String representing player's password
	 * 
	 * @return boolean indicating whether or not new account 
	 * 		   successfully created and new player logged in
	 */
	public boolean createAccount(String username, String password) {
		if (accountDictionary.containsKey(username)) {			
			return false;
		} 
		else {
			// create new account
			PlayerAccount acc = new PlayerAccount(username, password);
			accountDictionary.put(username, acc);
			leaderboard.add(acc);
			writeAccountsFile();
			currPlayer = acc;
			puzzle = currPlayer.getPuzzle();
			notifyObservers(this);
			return true;
		}
	}
	
	/**
	 * Logs player out
	 */
	public void logout() {
		currPlayer = null;
		notifyObservers(this);
	}
	
	/**
	 * Receives and processes guess attempt for correctness
	 * 
	 * @param attempt String representing player's guess attempt at the puzzle word
	 * 
	 * @return An ArrayList of AssociationState objects representing user guess
	 */
	public ArrayList<AssociationState> processGuess(String attempt) {
		if (canMakeGuess()) {
			var result = puzzle.guess(attempt);
			if (result != null) {  // attempt is valid
				currPlayer.guessMade();
				ArrayList<AssociationState> coding = new ArrayList<>(result);
				
				if (didWin(attempt)) {
					currPlayer.gameWon();
					gameOver = true;
					won = true;
					leaderboardChanged();
					notifyObservers(this);
				}
				return coding;
			}
			// can make guess, but attempt was invalid
			return new ArrayList<AssociationState>();
			
		}
		// out of guesses
		gameOver = true;
		won = false;
		return null;
	}
	
	/**
	 * Returns current player
	 * 
	 * @return PlayerAccount representing the current player
	 */
	public PlayerAccount getCurrPlayer() {
		return currPlayer;
	}
	
	/**
	 * Checks if player won 
	 * 
	 * @param attempt String representing user's guess
	 * @return boolean indicating whether player won the game
	 */
	public boolean didWin(String attempt) {
		if (attempt.equals(puzzle.getWord())) {
			notifyObservers(this);
			return true;
		}
		notifyObservers(this);
		return false;
	}
	
	/**
	 * Indicates whether or not player won the game
	 * 
	 * @return boolean indicating whether or not player won the game
	 */
	public boolean gameWon() {
		return gameOver && won;
	}
	
	/**
	 * Indicates whether or not game is over 
	 * 
	 * @return boolean indicating whether or not game is over 
	 */
	public boolean isGameOver() {
		notifyObservers(this);
		return gameOver;
	}
	
	/**
	 * Returns the puzzle word
	 * 
	 * @return String representing the puzzle word
	 */
	public String getPuzzleWord() {
		return puzzle.getWord();
	}
	
	/**
	 * Restarts the game
	 */
	public void restart() {
		// For example, if player wants to play new game. Still logged in
		gameOver = false;
		won = false;
		currPlayer.newGame();
		puzzle = currPlayer.getPuzzle();
		notifyObservers(this);
	}
	
	/**
	 * Provides access to the leaderboard ArrayList
	 * 
	 * @return ArrayList of PlayerAccounts representing the global leaderboard.
	 */
	public ArrayList<PlayerAccount> getLeaderboard() {
		return leaderboard;
	}
	
	/**
	 * Removes an account from account dictionary
	 * 
	 * @param username String representing the username of the account to be removed
	 * 
	 * @return boolean indicating whether or not account was successfully removed
	 */
	public boolean removeAccount(String username) {
		for (String name : accountDictionary.keySet()) {
			if (name.equals(username)) {
				accountDictionary.remove(name);
				writeAccountsFile();
				return true;
			}
		}
		return false;
	}
	
	private boolean canMakeGuess() {
		if(currPlayer.exhaustedGuesses()) {
			currPlayer.gameLost();
			gameOver = true;
			won = false;
			notifyObservers(this);
			return false;
		}
		return true;
	}
	
	
	private void leaderboardChanged() {
		int index = leaderboard.indexOf(currPlayer) - 1;
		
		while((index >= 0) && (currPlayer.hasMoreWins(leaderboard.get(index)) 
				|| currPlayer.hasEqualWins(leaderboard.get(index)))) {
			double currAvg = currPlayer.getWeightedGuesses();
			double otherAvg = leaderboard.get(index).getWeightedGuesses();
			if(otherAvg <= currAvg) {
				break;
			}
			
			swapLeaderboard(index, index + 1);
			
			index--;
		}
		
		if(index >= 0) {
			leaderboard.set(index, currPlayer);
		}
	}
	
	
	private void restoreLeaderboard() {
		for(String name : accountDictionary.keySet()) {
			PlayerAccount acc = accountDictionary.get(name);
			leaderboard.add(acc);
		}
		
		// selection sort algorithm; can't use built in sort methods because
		// Comparable/Comparator were never implemented on PlayerAccount.
		for(int i = 0; i < leaderboard.size() - 1; i++) {
			int maxWins = i;
			for(int j = i + 1; j < leaderboard.size(); j++) {
				if(leaderboard.get(j).hasMoreWins(leaderboard.get(maxWins))) {
					maxWins = j;
				}
			}
			
			swapLeaderboard(maxWins, i);
		}
		
		// tiebreaker: use weighted average of the guesses. Whoever has a smaller
		// weighted average (ie fewer guesses needed to win) is higher on the board.
		for(int i = 0; i < leaderboard.size() - 1; i++) {
			if(leaderboard.get(i).hasEqualWins(leaderboard.get(i + 1))) {
				double currAvg = leaderboard.get(i).getWeightedGuesses();
				double nextAvg = leaderboard.get(i + 1).getWeightedGuesses();
				
				if(nextAvg < currAvg) {
					swapLeaderboard(i, i + 1);
				}
			}
		}
	}
	
	
	private void swapLeaderboard(int first, int second) {
		PlayerAccount temp = leaderboard.get(first);
		leaderboard.set(first, leaderboard.get(second));
		leaderboard.set(second, temp);
	}
	
	
	/**
	 * Reads in accounts from "accounts.ser" file
	 */
	public void readAccountsFile() {
		FileInputStream fromFile;
		try {
			fromFile = new FileInputStream("accounts.ser");
			ObjectInputStream inFile = new ObjectInputStream(fromFile);
			accountDictionary = (HashMap<String, PlayerAccount>) inFile.readObject();
			
			inFile.close();
			fromFile.close();
			
			restoreLeaderboard();
		} catch (FileNotFoundException err) {
			System.out.println("Input file not found");
		} catch (IOException err) {
			err.printStackTrace();
			System.out.println("Couldn't read from file");
		} catch (ClassNotFoundException err) {
			System.out.println("Incorrect cast");
		}
	}
	
	
	/**
	 * Writes accounts to "accounts.ser" file 
	 */
	public void writeAccountsFile() {
		FileOutputStream output;
		try {
			output = new FileOutputStream("accounts.ser");
			ObjectOutputStream outFile = new ObjectOutputStream(output);
			outFile.writeObject(accountDictionary);

			outFile.close();
			output.close();
		} catch (FileNotFoundException err) {
			System.out.println("Output file not found");
		} catch (IOException err) {
			err.printStackTrace();
			System.out.println("Couldn't write to file");
		}
	}
	
}
