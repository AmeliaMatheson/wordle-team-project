package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.WordList;

/**
 * Tests for the WordList class, which stores the list of all the words that can
 * be used in Wordle and provides some utilities related to it.
 * 
 * @author Adrianna Koppes
 * @since April 7, 2023
 */
class WordListTest {

	/**
	 * Tests for the getPuzzleWord method, which should return a random word for the
	 * current Wordle puzzle.
	 */
	@Test
	void testGetPuzzleWord() {
		WordList testList = new WordList();
		assertEquals("usher", testList.getPuzzleWord(125));
		assertEquals(1, testList.getNumberOfUsedWords());
		assertEquals("guile", testList.getPuzzleWord(20));
		assertEquals(2, testList.getNumberOfUsedWords());
		assertEquals("chafe", testList.getPuzzleWord(15));
		assertEquals(3, testList.getNumberOfUsedWords());
		assertEquals("union", testList.getPuzzleWord(24));
		assertEquals(4, testList.getNumberOfUsedWords());
		assertEquals("trust", testList.getPuzzleWord(75));
		assertEquals(5, testList.getNumberOfUsedWords());
	}

	/**
	 * Tests for getNumberOfUsedWords, which should return the number of words that
	 * have already been used in a recent puzzle.
	 */
	@Test
	void testGetNumberOfUsedWords() {
		WordList dictionary = new WordList();
		assertEquals(0, dictionary.getNumberOfUsedWords());

		dictionary.getPuzzleWord(-1); // get a completely random word
		assertEquals(1, dictionary.getNumberOfUsedWords());

		dictionary.getPuzzleWord(-1); // add another random word
		assertEquals(2, dictionary.getNumberOfUsedWords());

		// add five random words at once
		dictionary.getPuzzleWord(-1);
		dictionary.getPuzzleWord(-1);
		dictionary.getPuzzleWord(-1);
		dictionary.getPuzzleWord(-1);
		dictionary.getPuzzleWord(-1);
		assertEquals(7, dictionary.getNumberOfUsedWords());
	}

	/**
	 * Tests for contains, which should return true or false depending on whether
	 * the provided word is in the dictionary or not.
	 */
	@Test
	void testContains() {
		WordList list = new WordList();
		assertTrue(list.contains("funky"));
		assertTrue(list.contains("hover"));
		assertTrue(list.contains("ozone"));
		assertTrue(list.contains("beret"));
		assertTrue(list.contains("scowl"));
		assertTrue(list.contains("sumac"));
		assertTrue(list.contains("flail"));

		assertFalse(list.contains(""));
		assertFalse(list.contains(null));
		assertFalse(list.contains("funky\n"));
		assertFalse(list.contains("scowl "));
		assertFalse(list.contains(" scowl"));
		assertFalse(list.contains("\thover"));
		assertFalse(list.contains("\nberet"));
		assertFalse(list.contains("ozone\t"));
		assertFalse(list.contains("abcde"));
		assertFalse(list.contains("cats"));
		assertFalse(list.contains("scowls"));
		assertFalse(list.contains("swing\nafoul"));
		assertFalse(list.contains("magmajoint"));
	}
}
