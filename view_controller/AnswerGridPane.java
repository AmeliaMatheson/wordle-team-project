package view_controller;


import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import model.AssociationState;
import model.LetterState;
import model.WordleBackend;

/**
 * Pane to display the answer grid and user's guesses as they are made.
 * Tiles are color coded with respect to guess correctness
 * 
 * @author Amelia Matheson and Adrianna Koppes
 * @since April 19, 2023
 */

public class AnswerGridPane extends GridPane {

	private LetterTile[][] grid;
	private ArrayList<ArrayList<LetterTile>> tileList;
	private ObservableList<ArrayList<LetterTile>> tiles;
	private WordleBackend backend;
	private Keyboard keyboard;
	private String guessAttempt = "";
	private int row = 0;
	private int col = 0;
	private String backgroundColor = "white";

	public AnswerGridPane(WordleBackend game, Keyboard givenKeyboard) {
		backend = game;
		keyboard = givenKeyboard;
		grid = new LetterTile[6][5];
		tileList = new ArrayList<>();
		backend.readAccountsFile();

		this.setPadding(new Insets(7, 0, 7, 3));
		this.setStyle("-fx-background-color: " + backgroundColor + ";");
		makeGrid();
		retrieveTiles();
	}
	
	/**
	 * Updates the grid by filling in saved guessed when applicable  
	 */
	public void updateGrid() {
		if(isGridEmpty()) {
			retrieveTiles();
		}
		if(backend.getCurrPlayer() == null) {
			grid = new LetterTile[6][5];
			tileList = new ArrayList<>();
			makeGrid();
		}
	}
	
	/**
	 * Sets Backend object for this AnswerGridPane
	 * 
	 * @param backend A Wordle Backend responsible for controlling the game
	 */
	public void setBackend(WordleBackend backend) {
		this.backend = new WordleBackend();
		
	}
	
	/**
	 * Changes mode (light or dark)
	 */
	public void changeMode() {
		String mode = "";
		if (backgroundColor.equals("white")) {
			mode = "black";
			this.setStyle("-fx-background-color: " + "black" + ";");
			backgroundColor = "black";
		} 
		else {
			mode = "white";
			this.setStyle("-fx-background-color: " + "white" + ";");
			backgroundColor = "white";
		}
		
		if (tiles != null) {
			changeTilesMode(mode);
		}
//		for (ArrayList<LetterTile> group : tiles) {
//			for (LetterTile tile : group) {
//				if (tile != null) {
//				tile.changeMode(mode);
//				}
//			}
//		}
	}
	
	private void changeTilesMode(String mode) {
		for (ArrayList<LetterTile> group : tiles) {
			for (LetterTile tile : group) {
				if (tile != null) {
				tile.changeMode(mode);
				}
			}
		}
	}
	
	/**
	 * Begins a new game by reseting AnswerGridPane to default settings
	 */
	public void newGame() {
		for (ArrayList<LetterTile> group : tiles) {
			for (LetterTile tile : group) {
				tile.reset(backgroundColor);
			}
		}
		row = col = 0;
		guessAttempt = "";
		keyboard.resetKeyboard();
	}
	
	
	private void retrieveTiles() {
		if(backend.getCurrPlayer() == null) {
			return;
		}
		ArrayList<ArrayList<String>> saved = backend.getCurrPlayer().retrieveSaved();
		ArrayList<String> oldWords = backend.getCurrPlayer().getOldGuesses();
		boolean hasWord = false;
		
		for(int i = 0; i < saved.size(); i++) {
			char[] oldWord = new char[5];
			if(i < oldWords.size()) {
				oldWord = oldWords.get(i).toCharArray();
				hasWord = true;
			}
			else {
				hasWord = false;
			}
			// set tile letters using String in saved
			for(int j = 0; j < saved.get(i).size(); j++) {
				if(saved.get(i).get(j).equals("")) {  
					tileList.get(i).get(j).reset(backgroundColor);
					continue;
				}
				if(hasWord) {
					tileList.get(i).get(j).setLetter(String.valueOf(oldWord[j]));
				}
				tileList.get(i).get(j).setColor(saved.get(i).get(j));
			}
		}
		row = oldWords.size();
		tiles = FXCollections.observableArrayList(tileList);
		changeTilesMode(backgroundColor);
		registerHandlers();
	}


	private void makeGrid() {
		this.setVgap(5);
		this.setHgap(5);
		for (int i = 0; i < 6; i++) {
			tileList.add(new ArrayList<>());
			for (int j = 0; j < 5; j++) {
				LetterTile tile = new LetterTile();
				grid[i][j] = tile;
				tileList.get(i).add(tile);
				this.add(tile, j+2, i);
			}
		}
	}


	private void updateSaved() {
		ArrayList<ArrayList<String>> toSave = new ArrayList<>();
		for(int i = 0; i < 6; i++) {
			toSave.add(new ArrayList<String>());
			for(int j = 0; j < 5; j++) {
				toSave.get(i).add(j, tileList.get(i).get(j).getColor());
			}
		}
		backend.getCurrPlayer().saveGrid(toSave); 
	}
	

	private boolean isGridEmpty() { 
		for(int i = 0; i < tileList.size(); i++) {
			for(int j = 0; j < tileList.get(i).size(); j++) {
				LetterTile currTile = tileList.get(i).get(j);
				if(!(currTile.getColor().equals(""))) {
					return false;
				}
			}
		}
		return true;
	}
	

	private class KeyHandler implements EventHandler<KeyEvent> {
	
		@Override
		public void handle(KeyEvent event) {
			// player has entered five letters
			if (col == 5) {
				if (event.getCode().isLetterKey()) {
					return;
				}
				
				if (event.getCode().equals(KeyCode.ENTER)) {
					var result = backend.processGuess(guessAttempt.toLowerCase());
					col = 0;
					if (result != null) {  // encountered invalid result
						if(result.size() == 0) {
							for(int c = 0; c < 5; c++) {
								tileList.get(row).get(c).setLetter("");
							}
							invalidAlert();
							col = 0;
							guessAttempt = "";
							return;
						}
						
						// encountered valid result
						assignColors(result, row);
						setTileLetters(row);
						keyboard.updateKeyboard(result);
						updateSaved();
						backend.getCurrPlayer().addNewSavedGuess(guessAttempt);
						if(backend.gameWon()) {
							String[] newAchievementList = backend.getCurrPlayer().getNewAchievements();
							for (int i = 0; i < 10; i++) {
								if (newAchievementList[i] != "") {
									achievementAlert(newAchievementList[i]);
								}
							}
							wonAlert();
							return;
						}
						col = 0;
						row++;
						guessAttempt = "";
						registerHandlers();
						if(row == 6) {
							exhaustedGuessesAlert();
						}
					}
					
					// result is null, so player must be out of guesses
					else {
						exhaustedGuessesAlert();
					}
				}
			}
			
			if (event.getCode().equals(KeyCode.BACK_SPACE)) {
				if (col == 0) {
					return;
				}
				col--;
				guessAttempt = guessAttempt.substring(0, guessAttempt.length()-1);
				tileList.get(row).get(col).setLetter("");
			} 
			
			if (event.getCode().isLetterKey()){
				tileList.get(row).get(col).setLetter(event.getCode().toString());
				col++;
				guessAttempt += event.getCode().toString();
				
			}
		}
	}


	private void registerHandlers() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				grid[i][j].setOnKeyPressed(new KeyHandler());
			}
		}
	}
	

	private void wonAlert() {
		playSound();
		Alert temp = new Alert(AlertType.WARNING);
		temp.setTitle("Winner!");
		temp.setHeaderText("Congratulations! You won.");
		temp.setContentText("Close this alert to start a new game.");
		temp.show();
		temp.setOnCloseRequest((closeEvent) -> {
			temp.close();
			closeAndRestart();
		});
	}
	
	private void achievementAlert(String newAchievement) {
		Alert achievements = new Alert(AlertType.WARNING);
		achievements.setTitle("New Achievement!");
		achievements.setHeaderText("Congratulations! You have made a new achievement!.");
		achievements.setContentText(newAchievement);
		achievements.show();
		achievements.setOnCloseRequest((closeEvent) -> {
			achievements.close();
		});
	}
	

	private void invalidAlert() {
		Alert invalid = new Alert(AlertType.WARNING);
		invalid.setTitle("Invalid word");
		invalid.setHeaderText("Sorry, that is not a word.");
		invalid.setContentText("Please enter a valid word.");
		invalid.show();
		invalid.setOnCloseRequest((event) -> {
			invalid.close();
		});
	}
	

	private void exhaustedGuessesAlert() {
		Alert temp = new Alert(AlertType.WARNING);
		temp.setTitle("Better luck next time");
		temp.setHeaderText("Guesses exhausted. The word was " + backend.getPuzzleWord());
		temp.setContentText("Close this alert to start a new game.");
		temp.show();
		temp.setOnCloseRequest((closeEvent) -> {
			temp.close();
			closeAndRestart();
		});
	}
	

	private void playSound() {
		File file = new File("GameWin.mp3");
		URI uri = file.toURI();
		Media media = new Media(uri.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}
	

	private void assignColors(ArrayList<AssociationState> coding, int toAssign) {
		for(int i = 0; i < coding.size(); i++) {
			tileList.get(toAssign).get(i).setColor(colorMatch(coding.get(i).state));
		}
	}
	

	private String colorMatch(LetterState state) {
		if(state.getValue() == Color.GREEN) {
			return "limegreen";
		}
		if (state.getValue() == Color.GRAY) {
			return "gray";
		}
		else {
			return "#C8B653";  //true Wordle yellow color (https://www.color-hex.com/color-palette/1012607)
		}
	}
	

	private void setTileLetters(int toSet) {
		char[] currGuess = guessAttempt.toCharArray();
		for(int i = 0; i < currGuess.length; i++) {
			tileList.get(toSet).get(i).setLetter(String.valueOf(currGuess[i]));
		}
	}
	
	
	private void closeAndRestart() {
		newGame();  
		row = 0;
		col = 0;
		backend.restart();
		registerHandlers();
	}

	
	
}