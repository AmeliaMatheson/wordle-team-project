package view_controller;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.PlayerAccount;
import model.WordleBackend;
import model.WordleObservable;
import model.WordleObserver;

/**
 * Main GUI that holds the menu for switching between panes. Panes include log
 * in, global and personal statistics, badges (WOW FACTOR), and the game itself.
 * The menu also allows the player to between light and dark mode and log out.
 * 
 * @author Blue Garrabrant
 * @since April 15, 2023
 */
public class WordleGUI extends Application implements WordleObserver {

	public static void main(String[] args) {
		launch(args);
	}

	public static final int width = 400;
	public static final int height = 620;

	private BorderPane window = new BorderPane();
	private MenuBar menuBar;
	private Menu settings;
	private Menu profile;
	private MenuItem personalStats;
	private MenuItem achievements;
	private MenuItem switchMode;
	private MenuItem globalStats;
	private MenuItem playGame;
	private MenuItem logOut;

	private WordleBackend backend = new WordleBackend();

	private LeaderboardPane leader;
	private AnswerGridPane gamePlayPane;
	private LogInPane logInPane;
	private Keyboard keyboard;
	private PersonalStatsPane statsPane;
	private Achievements achievementsPane;

	private int mode = 1; // 1 is light mode -1 is dark mode
	private String modeLabel = "Dark Mode";
	private String color = "white";
	private String text = "black";

	/**
	 * Sets up the GUI window for the program, as well as connecting the GUI (view
	 * and controller) to the backend (model).
	 * 
	 * @param stage Stage object representing the window to be used.
	 * @throws Exception if the GUI cannot be set up properly.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		backend.readAccountsFile();
		leader = new LeaderboardPane(backend.getLeaderboard());
		keyboard = new Keyboard(backend);
		gamePlayPane = new AnswerGridPane(backend, keyboard);
		logInPane = new LogInPane(backend);
		statsPane = new PersonalStatsPane(backend.getCurrPlayer());
		achievementsPane = new Achievements(backend);

		backend.add(this);

		stage.setTitle("WORDLE");
		menuBar = new MenuBar();
		window.setTop(menuBar);
		settings = new Menu("Settings");
		profile = new Menu("Profile");
		personalStats = new MenuItem("Personal Statistics");
		achievements = new MenuItem("Personal Achievements");
		switchMode = new MenuItem(modeLabel);
		globalStats = new MenuItem("Global Statistics");
		playGame = new MenuItem("Play Game");
		logOut = new MenuItem("Log Out");
		profile.getItems().addAll(personalStats, achievements, switchMode);
		settings.getItems().addAll(profile, globalStats, playGame, logOut);
		menuBar.getMenus().addAll(settings);
		window.setCenter(logInPane);

		Scene scene = new Scene(window, width, height);
		registerHandlers();
		stage.setScene(scene);
		stage.show();

		stage.setOnCloseRequest((event) -> {
			backend.writeAccountsFile();
		});
	}

	
	private void changeMode() {
		if (mode == 1) {
			modeLabel = "Light Mode";
			color = "black";
			text = "white";
		} else {
			modeLabel = "Dark Mode";
			color = "white";
			text = "black";
		}

		window.setStyle("-fx-background-color: " + color + ";");
		menuBar.setStyle("-fx-base: " + color + ";");

		gamePlayPane.changeMode();
		logInPane.changeMode();
		leader.changeColorMode();
		keyboard.changeMode();
		statsPane.changeMode();
		achievementsPane.changeMode();
		switchMode.setText(modeLabel);
		mode *= -1;
	}
	
	
	private void registerHandlers() {
		switchMode.setOnAction(new settingsChange());
		personalStats.setOnAction(new settingsChange());
		achievements.setOnAction(new settingsChange());
		globalStats.setOnAction(new settingsChange());
		playGame.setOnAction(new settingsChange());
		logOut.setOnAction(new settingsChange());

	}
	
	
	private class settingsChange implements EventHandler<ActionEvent> {

		/**
		 * Handles the user's interactions with the game menu.
		 * 
		 * @param ae ActionEvent representing the menu item selected.
		 */
		@Override
		public void handle(ActionEvent ae) {
			String text = ((MenuItem) ae.getSource()).getText();

			// switch between light and dark mode
			if (text.equals("Light Mode") || text.equals("Dark Mode")) {
				// FUTURE Needs to be fixed
				changeMode();
			}

			// show personal statistics pane
			if (text.equals("Personal Statistics")) {
				if (backend.getCurrPlayer() == null) {
					logInPane.needToLogIn("Personal Statistics");
					window.setCenter(logInPane);
					window.setBottom(null);

				} else {
					window.setCenter(statsPane);
					window.setBottom(null);
				}
			}

			// show achievements pane
			if (text.equals("Personal Achievements")) {
				if (backend.getCurrPlayer() == null) {
					logInPane.needToLogIn("Personal Achievements");
					window.setCenter(logInPane);
					window.setBottom(null);

				} else {
					window.setCenter(achievementsPane);
					window.setBottom(null);
				}
			}

			// show global statistics pane
			if (text.equals("Global Statistics")) {
				window.setCenter(leader);
				window.setBottom(null);
			}

			// show the game pane
			if (text.equals("Play Game")) {
				if (backend.getCurrPlayer() == null) {
					logInPane.needToLogIn("The Game");
					window.setCenter(logInPane);
					window.setBottom(null);
				} else {
					window.setCenter(gamePlayPane);
					window.setBottom(keyboard);
				}
			}

			// show log in pane and log out the player
			if (text.equals("Log Out")) {
				logInPane.needToLogIn("Log Out");
				window.setCenter(logInPane);
				window.setBottom(null);
				backend.logout();
			}
		}
	}

	/**
	 * Updates the board, keyboard, and stats pages for the game.
	 * 
	 * @param observed WordleObservable representing the observable for the game.
	 */
	@Override
	public void update(WordleObservable observed) {
		leader.updateLeaderboard(backend.getLeaderboard());
		gamePlayPane.updateGrid();
		keyboard.update();
		statsPane.updateStats(backend.getCurrPlayer());
		achievementsPane.update();
	}
}
