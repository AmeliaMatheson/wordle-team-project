package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.PlayerAccount;
import model.Puzzle;

/**
 * Tests for PlayerAccount.
 * 
 * @author Blue Garrabrant
 */
class PlayerAccountTest {

	@Test
	void playAccountTest() {
		PlayerAccount john = new PlayerAccount("John", "john123");
		assertEquals("John", john.getUsername());
		assertEquals("john123", john.getPassword());
		int[] pastWins = john.getPastWins();
		for (int i = 0; i < 6; i++) {
			assertEquals(0, pastWins[i]);
		}
		
		Puzzle johnPuzzle = john.getPuzzle();
		assertNotNull(johnPuzzle);
		assertFalse(john.exhaustedGuesses());
		assertEquals(0, john.getGuesses());
		assertEquals(0, john.getStreak());
		assertEquals(0, john.getLongestStreak());
		assertEquals(0, john.getPercentage());
		
		john.guessMade();
		assertEquals(1, john.getGuesses());
		
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		assertFalse(john.exhaustedGuesses());
		
		john.guessMade();
		assertTrue(john.exhaustedGuesses());
		
		john.gameWon();
		assertEquals(1, john.getStreak());
		
		john.newGame();
		assertNotEquals(johnPuzzle, john.getPuzzle());
		
		john.guessMade();
		john.guessMade();
		john.gameWon();
		assertEquals(2, john.getStreak());
		
		john.newGame();
		assertNotEquals(johnPuzzle, john.getPuzzle());
		
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.gameLost();
		assertEquals(0, john.getStreak());
		assertEquals(2, john.getLongestStreak());
		
		john.newGame();
		assertNotEquals(johnPuzzle, john.getPuzzle());
		
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.gameLost();
		assertEquals(0, john.getStreak());
		assertEquals(2, john.getLongestStreak());
		
		john.newGame();
		assertNotEquals(johnPuzzle, john.getPuzzle());

		pastWins = john.getPastWins();
		assertEquals(0, pastWins[0]);
		assertEquals(1, pastWins[1]);
		assertEquals(0, pastWins[2]);
		assertEquals(0, pastWins[3]);
		assertEquals(0, pastWins[4]);
		assertEquals(1, pastWins[5]);
		
		assertEquals(50, john.getPercentage());
		
		assertEquals(4, john.getTotalGames());
		
		john.guessMade();
		john.gameWon();
		john.guessMade();
		john.gameWon();
		john.guessMade();
		john.gameWon();
		assertEquals(3, john.getLongestStreak());

		

	}
	
	@Test
	void testComparisons() {
		PlayerAccount bob = new PlayerAccount("Bob", "12345");
		PlayerAccount lisa = new PlayerAccount("Lisa", "23456");
		
		assertTrue(bob.hasEqualWins(lisa));
		assertTrue(lisa.hasEqualWins(bob));
		
		bob.guessMade();
		bob.guessMade();
		bob.gameWon();
		assertEquals(bob.getWeightedGuesses(), 2.0/21.0, 0.0001);
		
		lisa.guessMade();
		lisa.guessMade();
		lisa.guessMade();
		lisa.gameWon();
		assertEquals(lisa.getWeightedGuesses(), 3.0/21.0, 0.0001);
		
		assertTrue(bob.hasEqualWins(lisa));
		assertTrue(lisa.hasEqualWins(bob));
		
		bob.guessMade();
		bob.guessMade();
		bob.guessMade();
		bob.guessMade();
		bob.guessMade();
		bob.gameWon();
		assertEquals(bob.getWeightedGuesses(), 7.0/21.0, 0.0001);
		
		assertFalse(bob.hasEqualWins(lisa));
		assertFalse(lisa.hasEqualWins(bob));
		
		assertTrue(bob.hasMoreWins(lisa));
		assertFalse(lisa.hasMoreWins(bob));
		
		lisa.guessMade();
		lisa.gameWon();
		assertEquals(lisa.getWeightedGuesses(), 4.0/21.0, 0.0001);
		
		assertTrue(bob.hasEqualWins(lisa));
		assertTrue(lisa.hasEqualWins(bob));
		
		assertFalse(bob.hasMoreWins(lisa));
		assertFalse(lisa.hasMoreWins(bob));
	}
	
	@Test
	void testSaving() {
		PlayerAccount sam = new PlayerAccount("Sam", "abcde");
		ArrayList<ArrayList<String>> gridSoFar = new ArrayList<>();
		String[] colors = {"#bababa", "#bababa", "#bababa", "#bababa", "#bababa", "#bababa", "#bababa", 
				"#bababa", "#bababa", "#bababa", "#bababa", "#bababa", "#bababa", "#bababa", "#bababa",
				"#bababa", "#bababa", "#bababa", "#bababa", "#bababa", "#bababa", "#bababa", "#bababa", 
				"#bababa", "#bababa", "#bababa"};
		
		// make empty grid and add it
		for(int i = 0; i < 6; i++) {
			ArrayList<String> toAdd = new ArrayList<>();
			for(int j = 0; j < 5; j++) {
				toAdd.add("");
			}
			gridSoFar.add(toAdd);
		}
		
		sam.saveGrid(gridSoFar);
		
		assertEquals(gridSoFar, sam.retrieveSaved());
		
		gridSoFar.get(0).set(3, "limegreen");
		
		sam.saveGrid(gridSoFar);
		assertEquals(gridSoFar, sam.retrieveSaved());
		
		sam.addNewSavedGuess("choke");
		sam.addNewSavedGuess("snake");
		ArrayList<String> guesses = sam.getOldGuesses();
		assertEquals(2, guesses.size());
		// guesses are saved in all caps
		assertEquals("CHOKE", guesses.get(0));
		assertEquals("SNAKE", guesses.get(1));
		
		sam.saveKeyboard(colors);
		String[] savedColor = sam.retrieveKeyboard();
		
		assertEquals("#bababa", savedColor[0]);
		assertEquals("#bababa", savedColor[24]);
	}
	
	@Test
	void achievements() {
		PlayerAccount john = new PlayerAccount("John", "john123");
		boolean[] allFalse = {false, false, false, false, false, false, false, false, false, false};
		boolean[] achievements = john.getAchievements();
		for (int i = 0; i < 10; i++) {
			assertEquals(achievements[i], allFalse[i]);
		}
		
		john.guessMade();
		john.gameWon();
		boolean[] firstGuess = {true, false, false, false, false, false, false, false, true, false};
		achievements = john.getAchievements();
		for (int i = 0; i < 10; i++) {
			assertEquals(achievements[i], firstGuess[i]);
		}
		
		john.guessMade();
		john.guessMade();
		john.gameWon();
		
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.gameWon();
		
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.gameWon();
		
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.gameWon();
		boolean[] fiveWins = {true, true, false, false, false, true, false, false, true, false};
		achievements = john.getAchievements();
		for (int i = 0; i < 10; i++) {
			assertEquals(achievements[i], fiveWins[i]);
		}
		
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.guessMade();
		john.gameWon();
		boolean[] oneOfEach = {true, true, false, false, false, true, false, false, true, true};
		achievements = john.getAchievements();
		for (int i = 0; i < 10; i++) {
			assertEquals(achievements[i], oneOfEach[i]);
		}
		
		for (int i = 7; i < 11; i++) {
			john.guessMade();
			john.gameWon();
		}
		boolean[] tenWins = {true, true, true, false, false, true, true, false, true, true};
		achievements = john.getAchievements();
		for (int i = 0; i < 10; i++) {
			assertEquals(achievements[i], tenWins[i]);
		}
		
		for (int i = 11; i < 31; i++) {
			john.guessMade();
			john.gameWon();
		}
		
		boolean[] thirtyStreak = {true, true, true, false, false, true, true, true, true, true};
		achievements = john.getAchievements();
		for (int i = 0; i < 10; i++) {
			assertEquals(achievements[i], thirtyStreak[i]);
		}
		
		for (int i = 31; i < 51; i++) {
			john.guessMade();
			john.gameWon();
		}
		
		boolean[] fiftyWins = {true, true, true, true, false, true, true, true, true, true};
		achievements = john.getAchievements();
		for (int i = 0; i < 10; i++) {
			assertEquals(achievements[i], fiftyWins[i]);
		}
		
		for (int i = 51; i < 101; i++) {
			john.guessMade();
			john.gameWon();
		}
		
		boolean[] hundredWins = {true, true, true, true, true, true, true, true, true, true};
		achievements = john.getAchievements();
		for (int i = 0; i < 10; i++) {
			assertEquals(achievements[i], hundredWins[i]);
		}
		assertEquals(john.getNewAchievements()[4], "You have won 100 games!");
		
		john.guessMade();
		john.gameWon();
		achievements = john.getAchievements();
		for (int i = 0; i < 10; i++) {
			assertEquals(achievements[i], hundredWins[i]);
		}
	}

	
	 
}

