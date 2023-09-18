package view_controller;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.AssociationState;
import model.WordleBackend;


/**
 * Pane to display the keyboard as the user make guesses.
 * Displays the key color with respect to the user's guesses.
 * Keyboard does not contain buttons, it is not functional for typing.
 * 
 * @author Blue Garrabrant
 * @since April 24, 2023
 */
public class Keyboard extends VBox{

	private Label a = new Label("A");
	private Label b = new Label("B");
	private Label c = new Label("C");
	private Label d = new Label("D");
	private Label e = new Label("E");
	private Label f = new Label("F");
	private Label g = new Label("G");
	private Label h = new Label("H");
	private Label i = new Label("I");
	private Label j = new Label("J");
	private Label k = new Label("K");
	private Label l = new Label("L");
	private Label m = new Label("M");
	private Label n = new Label("N");
	private Label o = new Label("O");
	private Label p = new Label("P");
	private Label q = new Label("Q");
	private Label r = new Label("R");
	private Label s = new Label("S");
	private Label t = new Label("T");
	private Label u = new Label("U");
	private Label v = new Label("V");
	private Label w = new Label("W");
	private Label x = new Label("X");
	private Label y = new Label("Y");
	private Label z = new Label("Z");

	private HashMap<String, Integer> letters = new HashMap<>(); 
	
	private HBox row1 = new HBox(30);
	private HBox row2 = new HBox(30);
	private HBox row3 = new HBox(30);
	
	private WordleBackend backend;
	
	private int mode = 1;
	private Color color = Color.valueOf("#bababa");
	private String text = "Black";
	private boolean reset = true;
	
	private Label[] keys = {a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z};
	private Color[] keyColors = {color,color,color,color,color,color,color,
			color,color,color,color,color,color,color,color,color,color,color,
			color,color,color,color,color,color,color,color};
	
	
	public Keyboard(WordleBackend givenBackend) {
		backend = givenBackend;
		setLetters();
		setLabels();
		this.setSpacing(6);
		this.setPadding(new Insets(10));

		row1.setSpacing(4);
		row2.setSpacing(4);
		row3.setSpacing(4);
		
		row1.setAlignment(Pos.CENTER);
		row2.setAlignment(Pos.CENTER);
		row3.setAlignment(Pos.CENTER);

        row1.getChildren().addAll(q, w, e, r, t, y, u, i, o, p);
        row2.getChildren().addAll(a, s, d, f, g, h, j, k, l);
        row3.getChildren().addAll(z, x, c, v, b, n, m);
        
        this.getChildren().addAll(row1, row2, row3);
	}
	
	/**
	 * Updates keyboard with saved data when applicable.
	 */
	public void update() {
		if (backend.getCurrPlayer() == null) {
			resetKeyboard();
		}
		if (reset && !(backend.getCurrPlayer() == null) && !(backend.getCurrPlayer().retrieveKeyboard() == null)) {
			setColors(backend.getCurrPlayer().retrieveKeyboard());
		}
	}
	
	
	private String[] getColors() {
		String[] colorStrings = {"", "", "", "", "", "", "", "", "", "", "", "", 
				"", "", "", "", "", "", "", "", "", "", "", "", "", "", };
		for (int i = 0; i < 26; i++) {
			colorStrings[i] = (keyColors[i].toString());
		}
		return colorStrings;
	}
	
	
	private void setColors(String[] colors) {
		for (int i = 0; i < 26; i++) {
			keyColors[i] = Color.valueOf(colors[i]);
			keys[i].setBackground(new Background(new BackgroundFill(keyColors[i], new CornerRadii(3), Insets.EMPTY)));
			keys[i].setStyle("-fx-text-fill: " + text + ";");
			if (!keyColors[i].equals(color)) {
				keys[i].setStyle("-fx-text-fill: " + "White" + ";");
			}
		}
		
	}
	
	
	private void setLabels() {
		for (int i = 0; i < 26; i++) {
			keys[i].setStyle("-fx-text-fill: " + text + ";");
			keys[i].setFont(Font.font("Verdana", FontWeight.BOLD, 10));
			keys[i].setAlignment(Pos.CENTER);
			keys[i].setMinWidth(34);
			keys[i].setMinHeight(50);
			keys[i].setBackground(new Background(new BackgroundFill(keyColors[i], new CornerRadii(3), Insets.EMPTY)));
		}
	}
	
	
	private void setLetters() {
		letters.put("A", 0);
		letters.put("B", 1);
		letters.put("C", 2);
		letters.put("D", 3);
		letters.put("E", 4);
		letters.put("F", 5);
		letters.put("G", 6);
		letters.put("H", 7);
		letters.put("I", 8);
		letters.put("J", 9);
		letters.put("K", 10);
		letters.put("L", 11);
		letters.put("M", 12);
		letters.put("N", 13);
		letters.put("O", 14);
		letters.put("P", 15);
		letters.put("Q", 16);
		letters.put("R", 17);
		letters.put("S", 18);
		letters.put("T", 19);
		letters.put("U", 20);
		letters.put("V", 21);
		letters.put("W", 22);
		letters.put("X", 23);
		letters.put("Y", 24);
		letters.put("Z", 25);
	}
	
	/**
	 * Switches colors of letters to match the users new guess.
	 * 
	 * @param coding ArrayList of AssociationState objects representing 
	 * 		  correctness of each letter in guess.
	 */
	public void updateKeyboard(ArrayList<AssociationState> coding){
		for(int i = 0; i < coding.size(); i++) {
			String letter = coding.get(i).letter.toUpperCase();
			int letterIndex = letters.get(letter);
			Color newColor = colorChange(pickColor(keyColors[letterIndex], coding.get(i).state.getValue()));
			keyColors[letterIndex] = newColor;
			keys[letterIndex].setBackground(new Background(new BackgroundFill(newColor, new CornerRadii(3), Insets.EMPTY)));
			if (!newColor.equals(color)) {
				keys[letterIndex].setStyle("-fx-text-fill: " + "White" + ";");
			}
		}
		reset = false;
		backend.getCurrPlayer().saveKeyboard(this.getColors());
	}
	
	/**
	 * Sets the keyboard back to original state with no set colors.
	 */
	public void resetKeyboard(){
		for (int i = 0; i < 26; i++) {
			keyColors[i] = color;
			keys[i].setBackground(new Background(new BackgroundFill(color, new CornerRadii(3), Insets.EMPTY)));
			keys[i].setStyle("-fx-text-fill: " + text + ";");
		}
		if (backend.getCurrPlayer() != null) {
			backend.getCurrPlayer().saveKeyboard(this.getColors());
		}
		reset = true;
	}
	
	
	private Color colorChange(Color setColor) {
		// switches to true colors
		if(setColor.equals(Color.GREEN) || setColor.equals(Color.valueOf("limegreen"))) {
			return Color.valueOf("limegreen");
		}
		if (setColor.equals(Color.GRAY) || setColor.equals(Color.valueOf("gray"))) {
			return Color.valueOf("gray");
		}
		if (setColor.equals(Color.YELLOW) || setColor.equals(Color.valueOf("#C8B653"))) {
			return Color.valueOf("#C8B653");
		}
		return color;
	}
	
	
	private Color pickColor(Color oldColor, Color newColor) {
		// picks the color that has precedence over the other
		if (oldColor.equals(Color.GREEN) || oldColor.equals(Color.valueOf("limegreen"))) {
			return oldColor;
		}
		if (newColor.equals(Color.GREEN) || newColor.equals(Color.valueOf("limegreen"))) {
			return newColor;
		}
		if (oldColor.equals(Color.YELLOW) || oldColor.equals(Color.valueOf("#C8B653"))) {
			return oldColor;
		}
		return newColor;
	}
	
	/**
	 * Switch between light and dark mode.
	 */
	public void changeMode() {
		if (mode == 1) {
			text = "White";
			for (int i = 0; i < 26; i++) {
					keys[i].setStyle("-fx-text-fill: " + text + ";");
			}

		} else {
			text = "Black";
			for (int i = 0; i < 26; i++) {
				if (keyColors[i].equals(color)) {
					keys[i].setStyle("-fx-text-fill: " + text + ";");
				}
			}
		}
		
		mode *= -1;
	}
	
}