package model;

import java.io.Serializable;

import javafx.scene.paint.Color;

/**
 * This enum represents the color that a LetterTile object can be by 
 * using JavaFX colors.
 * 
 * @author Amelia Matheson
 * @since April 19, 2023
 *
 */

public enum LetterState implements Serializable{
	GREEN(Color.GREEN),
	YELLOW(Color.YELLOW),
	GRAY(Color.GRAY);
	
	private Color value;
	
	private LetterState(Color color) {
		value = color;
	}
	
	/**
	 * Return value of enum element, a color
	 * 
	 * @return JavaFX Color
	 */
	public Color getValue() {
		return value;
	}
}
