package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Puzzle class represents the word of the day. It also indicates the correctness
 * of a guess attempt by coding each letter with respective colors.  
 * 
 * @author Amelia Matheson
 * @since April 8, 2023
 *
 */

public class Puzzle implements Serializable{
	private static final long serialVersionUID = 395048122194616710L;
	
	private LetterState correct = LetterState.GREEN;
	private LetterState inWord = LetterState.YELLOW;
	private LetterState incorrect = LetterState.GRAY;
	private String word;
	private WordList wordList = new WordList();
	
	
	public Puzzle() {
		word = setPuzzleWord();
		
	}
	
	
	/**
	 * Returns the puzzle word
	 * 
	 * @return The puzzle word
	 */
	public String getWord() {
		return word;
	}
	
	
	/**
	 * Checks guess attempt against puzzle word and color-codes letters accordingly
	 * 
	 * @param attempt The player's guess attempt at the puzzle word
	 * 
	 * @return An ArrayList of AssociationState objects
	 */
	public ArrayList<AssociationState> guess(String attempt) {
		ArrayList<AssociationState> coding = new ArrayList<>();
		HashMap<String, Integer> puzzCounts = new HashMap<>();
		
		char[] attempArr = attempt.toCharArray();
		char[] correctArr = word.toCharArray();
		
		if (!wordList.contains(attempt)) {
			return null;
		}
		
		// get instance count of letters in puzzle word
		for (int i = 0; i < correctArr.length; i++) {
			String l = String.valueOf(correctArr[i]);
			if (!puzzCounts.containsKey(l)) {
				puzzCounts.put(l, 0);
			}
			puzzCounts.put(l, puzzCounts.get(l) + 1);
		}
		
		// find correct letters first
		for (int i = 0; i < attempArr.length; i++) {
			String c = String.valueOf(attempArr[i]);
			if (attempArr[i] == correctArr[i]) {
				coding.add(new AssociationState(c, correct));
				puzzCounts.put(c, puzzCounts.get(c) - 1);
			}
			else {
				coding.add(new AssociationState("", incorrect));
			}
		}
		
		// now find in-word and incorrect letters
		for (int i = 0; i < attempArr.length; i++) {
			String c = String.valueOf(attempArr[i]);
			
			if (coding.get(i).state != correct) {
				if (word.contains(c) && puzzCounts.get(c) > 0) {
					coding.get(i).letter = c;
					coding.get(i).state = inWord;
					puzzCounts.put(c, puzzCounts.get(c) - 1);
				}
				else {
					coding.get(i).letter = c;
					coding.get(i).state = incorrect;
				}
			}
		}
		
		return coding;
	}
	
	
	private String setPuzzleWord() {
		return wordList.getPuzzleWord(-1);
	}
	
}
