package model;

import java.io.Serializable;

/**
 * Acts as a key-value pair that associates a letter with its state.
 * The fields are public for easy access to the stored data, which must be
 * accessed and potentially changed often.
 * 
 * @author Adrianna Koppes
 * @since April 19, 2023
 */
public class AssociationState implements Serializable {
	public String letter;
	public LetterState state;
	
	public AssociationState(String letter, LetterState state) {
		this.letter = letter;
		this.state = state;
	}
}
