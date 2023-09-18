package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

/**
 * WordList stores the list of Wordle words for the run of the program. It gets
 * these words from a words.txt file that contains all of the words in Wordle.
 * Credit to Finn Kunkele for this file. The class also has some utilities
 * related to the list of words, such as checking if the word is in the list.
 * Serialization is needed, but should be implemented in the Puzzle class so
 * that the entire word list's state is saved.
 * 
 * @author Adrianna Koppes
 * @since April 7, 2023
 */
public class WordList implements Serializable {
	private ArrayList<String> allWords;
	private HashSet<String> usedWords;
	private static final String FILE_NAME = "words.txt";
	
	public WordList() {
		usedWords = new HashSet<>();
		allWords = new ArrayList<>();

		Scanner reader;
		File file = new File(FILE_NAME);
		try {
			reader = new Scanner(file);

			while (reader.hasNext()) {
				// each line of the file is one word
				String word = reader.nextLine().trim();
				if (!(word.equals(""))) {
					// guard in case the Scanner picks up an empty line
					allWords.add(word);
				}
			}

			reader.close();
		}
		// should never be any errors, so just do nothing if they are reached
		catch (Exception err) {
		}
	}

	/**
	 * getPuzzleWord gets a word for the new Wordle puzzle, picking it from the
	 * words that have not been used yet. The word is randomly chosen.
	 * 
	 * @param seed Integer representing the seed for the random word generator.
	 *             Used only during testing; should be -1 when not testing.
	 * @return String representing the word that will now be the Wordle puzzle.
	 */
	public String getPuzzleWord(int seed) {
		String choose = new String();
		Random picker = new Random();
		if (seed != -1) {
			picker.setSeed(seed);
		}
		int rand;
		boolean keepLooking = true;
		do {
			rand = picker.nextInt(allWords.size());
			choose = allWords.get(rand);

			if (!(usedWords.contains(choose))) {
				keepLooking = false;
			}
		} while (keepLooking);

		usedWords.add(choose);
		return choose;
	}

	/**
	 * Determines if the word list contains the given word.
	 * 
	 * @param word String representing the word to be found
	 * @return boolean representing whether the given word is in the Wordle
	 *         dictionary. True if it is, false otherwise.
	 */
	public boolean contains(String word) {
		return allWords.contains(word);
	}

	/**
	 * Returns the number of words that have already been used in a Wordle puzzle,
	 * at least since the last time the list of used words was reset. Mostly for
	 * testing purposes.
	 * 
	 * @return integer representing the number of words that have already been used
	 *         in a Wordle puzzle.
	 */
	public int getNumberOfUsedWords() {
		return usedWords.size();
	}
}
