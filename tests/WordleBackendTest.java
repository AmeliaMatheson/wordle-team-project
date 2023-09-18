package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import model.PlayerAccount;
import model.Puzzle;
import model.WordleBackend;

/**
 * Tests for the WordleBackend class.
 * IMPORTANT NOTE: This JUnit test class is no longer relevant because we
 * found out that we do not need JUnit tests for anything to do with 
 * serialization, and this class is basically entirely about serialization.
 * Please ignore it.
 * 
 * @author Amelia Matheson and Adrianna Koppes
 * @since April 11, 2023
 */
class WordleBackendTest {

	@Test
	void constructorTest() {
		WordleBackend backend = new WordleBackend();
		assertEquals(null, backend.getCurrPlayer());
		assertFalse(backend.isGameOver());
		
	}
	
	@Test
	void loginCreateAccTest() {
		WordleBackend backend = new WordleBackend();
		backend.readAccountsFile();
		assertFalse(backend.login("amelia", "hello"));
		assertFalse(backend.login("rick", "rey"));
		assertFalse(backend.login("Exampleuser", "111"));
		assertTrue(backend.login("Exampleuser", "1111"));
		assertFalse(backend.createAccount("Exampleuser", "12"));
		assertTrue(backend.removeAccount("TestAcc"));
		assertFalse(backend.removeAccount("NotExisting"));
	}
	
	@Test
	void playTest() {
		WordleBackend backend = new WordleBackend();
		backend.readAccountsFile();
		backend.login("TestAcc", "1");
		var result = backend.processGuess("hello");
		assertFalse(result == null);
		backend.processGuess("pines");
		backend.processGuess("piney");
		backend.processGuess("apple");
		backend.processGuess("shone");
		backend.processGuess("bison");
		backend.processGuess("foggy");
		assertTrue(backend.processGuess("world") == null);
		backend.restart();
		assertFalse(backend.isGameOver());
	}
	
	@Test
	void winTest() {
		WordleBackend backend = new WordleBackend();
		backend.readAccountsFile();
		assertTrue(backend.login("Exampleuser", "1111"));
		String word = backend.getCurrPlayer().getPuzzle().getWord();
		backend.processGuess("chins");
		assertFalse(backend.didWin("chins"));
		backend.processGuess(word);
		assertTrue(backend.didWin(word));
	}
	
	
	@Test
	void logoutTest() {
		WordleBackend backend = new WordleBackend();
		backend.readAccountsFile();
		backend.login("Exampleuser", "1111");
		assertTrue(backend.getCurrPlayer() != null);
		backend.logout();
		assertEquals(null, backend.getCurrPlayer());
	}
	
	@Test
	void getLeaderboardTest() {
		WordleBackend backend = new WordleBackend();
		backend.readAccountsFile();
		assertNotNull(backend.getLeaderboard());
		
		backend.login("A_User", "helloworld");
		String word = backend.getCurrPlayer().getPuzzle().getWord();
		backend.processGuess(word);
		word = backend.getCurrPlayer().getPuzzle().getWord();
		backend.processGuess(word);
		word = backend.getCurrPlayer().getPuzzle().getWord();
		backend.processGuess(word);
		word = backend.getCurrPlayer().getPuzzle().getWord();
		backend.processGuess(word);
		assertNotNull(backend.getLeaderboard());
	}
	

}
