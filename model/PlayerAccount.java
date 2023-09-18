package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 * PlayerAccount stores information for the player's username, password, guesses made,
 * previous game wins, and save information for current games to be accesses if 
 * player is logged out.
 * 
 * @author Blue Garrabrant
 * @since April 15, 2023
 */

public class PlayerAccount implements Serializable{
	
	private Puzzle puzzle;
	private String username;
	private String password;
	private int guesses;
	private int[] pastWins = {0,0,0,0,0,0};
	private int streak;
	private int longestStreak;
	private double totalGames;
	private double totalWins;
	private ArrayList<ArrayList<String>> saved;
	private ArrayList<String> oldGuesses;
	private String[] keyboard;
	private boolean[] achievements = {false, false, false, false, false, 
			false, false, false, false, false};
	private String[] newAchievements = {"", "", "", "", "", "", "", "", "", "", "", ""};

	
	/**
	 * Creates the player's account with the given username and password.
	 * 
	 * @param givenUsername String representing the player's username.
	 * @param givenPassword String representing the player's password.
	 */
	public PlayerAccount(String givenUsername, String givenPassword) {
		puzzle = new Puzzle();
		username = givenUsername;
		password = givenPassword;
		guesses = 0;
		streak = 0;
		longestStreak = 0;
		totalGames = 0;
		totalWins = 0;
		saved = new ArrayList<>();
		oldGuesses = new ArrayList<>();
	}
	
	/**
	 * Returns the username for the player.
	 * 
	 * @return String representing the player's username.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns the password for the player.
	 * 
	 * @return String representing the player's password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Returns the past wins of the player organized by number of guesses.
	 * 
	 * @return List of Ints representing player's past wins.
	 */
	public int[] getPastWins() {
		return pastWins;
	}
	
	/**
	 * Returns the count of total games played by the player.
	 * 
	 * @return Double representing player's total games count.
	 */
	public double getTotalGames() {
		return totalGames;
	}
	
	/**
	 * Returns current streak of the player.
	 * 
	 * @return Int representing the player's streak.
	 */
	public int getStreak() {
		return streak;
	}
	
	/**
	 * Returns longest streak of the player.
	 * 
	 * @return Int representing the player's longest streak.
	 */
	public int getLongestStreak() {
		if (streak > longestStreak) {
			longestStreak = streak;
		}
		return longestStreak;
	}
	
	/**
	 * Returns percentage of the ratio of won games to total game for the player.
	 * 
	 * @return Double representing the player's win percentage.
	 */
	public double getPercentage() {
		if (totalGames == 0) {
			return 0;
		}
		return totalWins/totalGames * 100;
	}
	
	/**
	 * Updates the guess counter for the player.
	 * 
	 */
	public void guessMade() {
		guesses += 1;
	}
	
	/**
	 * Returns guess count for the player for the current game.
	 * 
	 * @return Int representing the player's guess count.
	 */
	public int getGuesses() {
		return guesses;
	}
	
	/**
	 * Updates the past wins, total games,total wins, guess count, streak and saved 
	 * guesses for a won game for the player. Checks if new achievements were made.
	 * 
	 */
	public void gameWon() {
		pastWins[guesses - 1] += 1;
		totalGames += 1;
		totalWins += 1;
		guesses = 0;
		streak += 1;
		saved = new ArrayList<>();
		oldGuesses = new ArrayList<>();
		checkAllAchievements();
	}
	
	/**
	 * Updates the longest streak, total games, guess count, streak and saved 
	 * guesses for a lost game for the player.
	 * 
	 */
	public void gameLost() {
		if (streak > longestStreak) {
			longestStreak = streak;
		}
		streak = 0;
		totalGames += 1;
		guesses = 0;
		saved = new ArrayList<>();
		oldGuesses = new ArrayList<>();
	}
	
	/**
	 * Returns whether or not the player has made the max number of guesses.
	 * 
	 * @return Boolean representing if the player has maxed out their guesses.
	 */
	public boolean exhaustedGuesses() {
		if (guesses > 5) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the puzzle for the player's current game.
	 * 
	 * @return Puzzle representing the current game.
	 */
	public Puzzle getPuzzle() {
		return puzzle;
	}
	
	/**
	 * Updates a new puzzle, guess count and saved 
	 * guesses for a new game to be played.
	 * 
	 */
	public void newGame() {
		puzzle = new Puzzle();
		guesses = 0;
		saved = new ArrayList<>();
		oldGuesses = new ArrayList<>();
	}
	
	/**
	 * Returns whether the player has more total wins than the given player.
	 * 
	 * @param toCheck PlayerAccount for the player to be compared against.
	 *              
	 * @return Boolean representing whether the player has more wins than given player.
	 */
	public boolean hasMoreWins(PlayerAccount toCheck) {
		return this.totalWins > toCheck.totalWins;
	}
	
	/**
	 * Returns a weighted average calculated by the players past wins.
	 * 
	 * @return Double representing player's weighted average.
	 */
	public double getWeightedGuesses() {
		double weightAvg = 0.0;
		for(int i = 1; i <= pastWins.length; i++) {
			double numWins = (i * pastWins[i - 1]) + 0.0;
			weightAvg += numWins;
		}
		weightAvg /= 21.0;	// 21 = 1+2+3+4+5+6 = sum of the weights
		return weightAvg;
	}
	
	/**
	 * Returns whether the player has an equal amount of total wins as the given player.
	 * 
	 * @param toCheck PlayerAccount for the player to be compared against.
	 *              
	 * @return Boolean representing whether the player equal wins as given player.
	 */
	public boolean hasEqualWins(PlayerAccount toCheck) {
		return this.totalWins == toCheck.totalWins;
	}
	
	/**
	 * Updates the arrayList of the player's previous guess.
	 * 
	 * @param word String of the players newest guess.
	 */
	public void addNewSavedGuess(String word) {
		oldGuesses.add(word.toUpperCase());
	}
	
	/**
	 * Returns the player's previous guesses.
	 * 
	 * @return ArrayList of Strings representing the player's saved guesses.
	 */
	public ArrayList<String> getOldGuesses() {
		return oldGuesses;
	}
	
	/**
	 * Updates the grid for the player's the current game.
	 * 
	 * @param toSave ArrayList of ArrayLists of Strings representing the current game.
	 */
	public void saveGrid(ArrayList<ArrayList<String>> toSave) {
		saved = toSave;
	}

	/**
	 * Returns the grid for the player's the current game.
	 * 
	 * @return ArrayList of ArrayLists of Strings representing the saved game.
	 */
	public ArrayList<ArrayList<String>> retrieveSaved() {
		return saved;
	}
	
	/**
	 * Updates the colors of the keyboard for the current game.
	 * 
	 * @param toSave List of Strings representing the colors of the keyboard.
	 */
	public void saveKeyboard(String[] toSave) {
		keyboard = toSave;
	}

	/**
	 * Returns the colors of the keyboard for the current game.
	 * 
	 * @return List of Strings representing the colors of the keyboard for saved game.
	 */
	public String[] retrieveKeyboard() {
		return keyboard;
	}
	
	/**
	 * Returns which achievements the player has made.
	 * 
	 * @return List of Booleans representing which achievements the player has made.
	 */
	public boolean[] getAchievements() {
		return achievements;
	}
	
	/**
	 * Returns which new achievements the player has made.
	 * 
	 * @return List of Strings representing new achievements made by the player.
	 */
	public String[] getNewAchievements() {
		String[] retval = {"", "", "", "", "", "", "", "", "", "", "", ""};
		for (int i = 0; i < 6; i++) {
			retval[i] = newAchievements[i];
			newAchievements[i] = "";
		}
		return retval;
	}
	
	
	private void checkAllAchievements() {
		for (int i = 0; i < 6; i++) {
			newAchievements[i] = "";
		}
		
		if(!achievements[0]) {
			newAchievements[0] = firstWin();
		}
		
		if(!achievements[1]) {
			newAchievements[1] = win5();
		}
		
		if(!achievements[2]) {
			newAchievements[2] = win10();
		}
		
		if(!achievements[3]) {
			newAchievements[3] = win50();
		}
		
		if(!achievements[4]) {
			newAchievements[4] = win100();
		}
		
		if(!achievements[5]) {
			newAchievements[5] = streak5();
		}
		
		if(!achievements[6]) {
			newAchievements[6] = streak10();
		}
		
		if(!achievements[7]) {
			newAchievements[7] = streak30();
		}
		
		if(!achievements[8]) {
			newAchievements[8] = firstGuess();
		}
		
		if(!achievements[9]) {
			newAchievements[9] = oneOfEach();
		}
	}
	
	
	private String firstWin() {
		if (totalWins >= 1) {
			achievements[0] = true;
			return "You have won your first game!";
		} else {return "";}
	}
	
	
	private String win5() {
		if (totalWins >= 5) {
			achievements[1] = true;
			return "You have won 5 games!";
		} else {return "";}
	}
	
	
	private String win10() {
		if (totalWins >= 10) {
			achievements[2] = true;
			return "You have won 10 games!";
		} else {return "";}
	}
	
	
	private String win50() {
		if (totalWins >= 50) {
			achievements[3] = true;
			return "You have won 50 games!";
		} else {return "";}
	}
	
	
	private String win100() {
		if (totalWins >= 100) {
			achievements[4] = true;
			return "You have won 100 games!";
		} else {return "";}
	}
	
	
	private String streak5() {
		if (streak >= 5) {
			achievements[5] = true;
			return "You have a 5 win streak!";
		} else {return "";}
	}
	
	
	private String streak10() {
		if (streak >= 10) {
			achievements[6] = true;
			return "You have a 10 win streak!";
		} else {return "";}
	}
	
	
	private String streak30() {
		if (streak >= 30) {
			achievements[7] = true;
			return "You have a 30 win streak!";
		} else {return "";}
	}
	
	
	private String firstGuess() {
		if (pastWins[0] >= 1) {
			achievements[8] = true;
			return "You won on your first guess!";
		} else {return "";}
	}
	
	
	private String oneOfEach() {
		boolean flag = true;
		for(int i = 0; i < 6; i++) {
			if (pastWins[i] < 1) {
				flag = false;
			}
		}
		if (flag) {
			achievements[9] = true;
			return "You have 6 games using 1, 2, 3, 4, 5 and 6 guesses!";
		} else {return "";}
		
	}
	
}
