package view_controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.PlayerAccount;

/**
 * Pane to display the leaderboard of players. Rankings are determined by wins,
 * or, if two or more users have the same number of wins, then the tie is broken
 * by whoever has a higher amount of their wins using few guesses.
 * 
 * @author Adrianna Koppes
 * @since April 16, 2023
 */
public class LeaderboardPane extends VBox {
	private LinkedHashMap<String, Integer> display;

	private int scrollHeight = 552;
	private ScrollPane scroller;
	private Canvas canvas;
	private GraphicsContext gc;
	private Label header;
	private String textColor = "black";
	private String backColor = "white";
	private Color canvasColor = Color.WHITE;
	private Color nameColor = Color.BLACK;
	
	public LeaderboardPane(ArrayList<PlayerAccount> curr) {
		display = new LinkedHashMap<>();
		scroller = new ScrollPane();
		canvas = new Canvas(383, scrollHeight);
		gc = canvas.getGraphicsContext2D();
		scroller.setContent(canvas);
		header = new Label("Global Leaderboard");
		header.setMaxWidth(Double.MAX_VALUE);
		header.setAlignment(Pos.CENTER);
		Font font = new Font("Arial", 30);
		header.setFont(font);
		updateLeaderboard(curr);
		style();
		this.getChildren().addAll(header, scroller);
	}

	/**
	 * Changes the color mode from light mode to dark mode or vice versa based on
	 * the current mode.
	 */
	public void changeColorMode() {
		if (textColor.equals("black") && backColor.equals("white")) {
			// currently in light mode; change to dark mode
			textColor = "white";
			backColor = "black";
			canvasColor = Color.BLACK;
			nameColor = Color.WHITE;
		} else {
			// currently in dark mode; change to light mode
			textColor = "black";
			backColor = "white";
			canvasColor = Color.WHITE;
			nameColor = Color.BLACK;
		}
		style();
	}

	/**
	 * Updates the leaderboard to the new state and then redraws it to the screen.
	 * 
	 * @param board ArrayList of PlayerAccounts representing the new, updated
	 *              leaderboard.
	 */
	public void updateLeaderboard(ArrayList<PlayerAccount> board) {
		display.clear();
		for (PlayerAccount acc : board) {
			String username = acc.getUsername();
			int wins = 0;
			int[] allWins = acc.getPastWins();
			for (int i = 0; i < allWins.length; i++) {
				wins += allWins[i];
			}
			display.put(username, wins);
		}
		int potentialHeight = 50 * display.size() + 20;
		if (potentialHeight > scrollHeight) {
			scrollHeight = potentialHeight;
		}
		style();
	}

	
	private void drawLeaderboard() {
		int rank = 1;
		int y = 20;
		int maxLen = 333;
		int len = maxLen;
		int maxScore = findMaxScore();
		Font font = new Font("Arial", 15);
		gc.setFont(font);

		for (String user : display.keySet()) {
			gc.setFill(Color.LIMEGREEN);
			int score = display.get(user);
			double percentage = score / (1.0 * maxScore);
			len = (int) (percentage * maxLen);
			gc.fillRect(30, y, len, 30);
			gc.setFill(nameColor);
			gc.fillText("" + rank, 15, y + 20);
			gc.fillText(user + " (" + score + ")", 40, y + 20);
			rank++;
			y += 50;
		}
	}

	
	private int findMaxScore() {
		int maxScore = Integer.MIN_VALUE;
		for (String user : display.keySet()) {
			int curr = display.get(user);
			if (curr > maxScore) {
				maxScore = curr;
			}
		}
		return maxScore;
	}

	
	private void style() {
		this.setStyle("-fx-background-color: " + backColor + ";");
		gc.setFill(canvasColor);
		gc.fillRect(0, 0, 400, scrollHeight);
		drawLeaderboard();
		scroller.setStyle("-fx-background-color: " + backColor + ";");
		header.setStyle("-fx-text-fill: " + textColor + ";");
	}
}
