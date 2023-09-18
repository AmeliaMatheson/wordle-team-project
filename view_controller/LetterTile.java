package view_controller;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Displays a single alphabetic character in user's guess by extending JavaFX Label'
 * Color is set with respect to complete guess correctness
 * 
 * @author Amelia Matheson
 * @since April 19, 2023
 *
 */

public class LetterTile extends Label{
	private String fillColor;
	private String textColor;
	private static final double HEIGHT = 70;
	private static final double WIDTH = 70;
	
	public LetterTile() {
		fillColor = "";
		textColor = "black";
		this.setPrefSize(HEIGHT, WIDTH);
		this.setFont(new Font("Arial", 32));
		this.setAlignment(Pos.CENTER);
		this.setTextAlignment(TextAlignment.CENTER);
		this.setStyle("-fx-background-color: " + "white" + ";"
				+ " -fx-font-weight: bold;"
				+ " -fx-text-fill: " + textColor + ";"
				+ " -fx-border-color: gainsboro;"
				+ " -fx-border-width: 2px;");
		this.setText("");
		this.setFocusTraversable(true);
	}
	
	/**
	 * Sets letter character of the tile
	 * 
	 * @param newChar An alphabetic String character to be displayed
	 */
	public void setLetter(String newChar) {
		this.setText(newChar);
	}
	
	/**
	 * Returns tile letter
	 * 
	 * @return The alphabetic String character displayed by the tile
	 */
	public String getLetter() {
		return this.getText();
	}
	
	/**
	 * Sets the background color of the tile
	 * 
	 * @param color A String representing a JavaFX color
	 */
	public void setColor(String color) {
		fillColor = color;
		this.setStyle("-fx-background-color: " + fillColor + ";"
				+ " -fx-font-weight: bold;"
				+ " -fx-text-fill: white;");
	}
	
	/**
	 * Returns the tile's background color
	 * 
	 * @return A String representing a JavaFX color
	 */
	public String getColor() {
		return fillColor;
	}
	
	/**
	 * Resets tile to default state, background color is black or white
	 * 
	 * @param mode String determining default background color 
	 */
	public void reset(String mode) {
		setLetter("");
		fillColor = "";
		if (mode.equals("white")) {
			textColor = "black";
		}
		else {
			textColor = "white";
		}
		this.setStyle("-fx-background-color: " + mode + ";"
				+ " -fx-font-weight: bold;"
				+ " -fx-text-fill: " + textColor + ";"
				+ " -fx-border-color: gainsboro;"
				+ " -fx-border-width: 2px;");
		this.setFocusTraversable(true);
	}
	
	/**
	 * Changes mode (light or dark) of tile
	 * 
	 * @param mode String determining default background color
	 */
	public void changeMode(String mode) {
		// only change mode if tile doesn't have fill color 
		if (fillColor.equals("")) {
			if (mode.equals("white")) {
				textColor = "black";
			}
			else {
				textColor = "white";
			}
			this.setStyle("-fx-background-color: " + mode + ";"
					+ " -fx-font-weight: bold;"
					+ " -fx-text-fill: " + textColor + ";"
					+ " -fx-border-color: gainsboro;"
					+ " -fx-border-width: 2px;");
			
		}
	}
}