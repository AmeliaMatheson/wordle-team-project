package view_controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.PlayerAccount;

/**
 * Pane displays personal statistics of a player once they are logged in.
 * 
 * @author Aspen Cross
 */

public class PersonalStatsPane extends BorderPane {
	// window panes
	private VBox centerItems;
	private VBox bottomItems;

	// items
	private Label gamesPlayedNumber;
	private Label winPercentNumber;
	private Label currentStreakNumber;
	private Label maxStreakNumber;
	private Label gamesPlayed;
	private Label winPercent;
	private Label currentStreak;
	private Label maxStreak;
	private BarChart<Number, String> guessDistributionChart;
	private XYChart.Series<Number, String> winsData;
	private NumberAxis xAxis;
	private CategoryAxis yAxis;

	// headers
	private Label welcomeHeader;
	private Label statisticsHeader;
	private Label guessDistributionHeader;

	// fonts and colors
	private Font headerFont;
	private Font numberFont;
	private Font textFont;
	private String textColor;
	private String backgroundColor;

	private PlayerAccount curr;

	public PersonalStatsPane(PlayerAccount curr) {
		this.curr = curr;
		textColor = "black";
		setUpFonts();
		setUpCenter();
		setUpBottom();
		setUpBorderPane();
		style();
	}

	/**
	 * Changes the background and text color to the proper black or white based on
	 * whether it is set to light or dark mode. initial set is in light mode
	 */
	public void changeMode() {
		// in light mode change to dark mode
		if (textColor.equals("black")) {
			textColor = "white";
			backgroundColor = "black";
		}
		// in dark mode change to light mode
		else {
			textColor = "black";
			backgroundColor = "white";
		}
		style();
	}

	/**
	 * Updates the statistics page each time the state of the game has changed
	 * 
	 * @param currPlayer PlayerAccount representing the current player.
	 */
	public void updateStats(PlayerAccount currPlayer) {
		curr = currPlayer;
		setUpCenter();
		setUpBottom();
		setUpBorderPane();
		style();
	}

	/*
	 * Sets up all elements in the border pane
	 */
	private void setUpBorderPane() {
		String username = new String();
		if (curr != null) {
			username = curr.getUsername();
		}
		welcomeHeader = new Label("Welcome " + username + "\n");
		welcomeHeader.setFont(headerFont);
		this.setTop(welcomeHeader);
		this.setCenter(centerItems);
		this.setBottom(bottomItems);
	}

	/*
	 * Sets up the different fonts used throughout the pane
	 */
	private void setUpFonts() {
		headerFont = new Font("Arial", 30);
		numberFont = new Font("Arial", 25);
		textFont = new Font("Arial", 15);
	}

	/*
	 * Sets up the statistics section. This section includes the games played, win
	 * percent, current streak, and max streak
	 */
	private void setUpCenter() {
		this.setPadding(new Insets(7));
		centerItems = new VBox();
		statisticsHeader = new Label("Statistics");
		statisticsHeader.setFont(headerFont);
		gamesPlayedNumber = new Label();
		gamesPlayedNumber.setFont(numberFont);
		winPercentNumber = new Label();
		winPercentNumber.setFont(numberFont);
		currentStreakNumber = new Label();
		currentStreakNumber.setFont(numberFont);
		maxStreakNumber = new Label();
		maxStreakNumber.setFont(numberFont);

		if (curr != null) {
			gamesPlayedNumber.setText(String.valueOf((int) curr.getTotalGames()));
			winPercentNumber.setText(String.valueOf(curr.getPercentage()));
			currentStreakNumber.setText(String.valueOf(curr.getStreak()));
			maxStreakNumber.setText(String.valueOf(curr.getLongestStreak()));
		}

		gamesPlayed = new Label("Played");
		gamesPlayed.setFont(textFont);
		winPercent = new Label("Win %");
		winPercent.setFont(textFont);
		currentStreak = new Label("Current Streak");
		currentStreak.setFont(textFont);
		currentStreak.setWrapText(true);
		maxStreak = new Label("Max Streak");
		maxStreak.setFont(textFont);

		// set header
		centerItems.getChildren().add(statisticsHeader);

		// set numbers and labels
		TilePane stats = new TilePane();
		stats.setMaxWidth(400);
		VBox playedGames = new VBox();
		playedGames.setMaxWidth(95);
		playedGames.getChildren().addAll(gamesPlayedNumber, gamesPlayed);
		VBox percentWin = new VBox();
		percentWin.setMaxWidth(95);
		percentWin.getChildren().addAll(winPercentNumber, winPercent);
		VBox streakCurr = new VBox();
		streakCurr.setMaxWidth(95);
		streakCurr.getChildren().addAll(currentStreakNumber, currentStreak);
		VBox streakMax = new VBox();
		streakMax.setMaxWidth(95);
		streakMax.getChildren().addAll(maxStreakNumber, maxStreak);

		stats.setPrefColumns(4);
		stats.getChildren().addAll(playedGames, percentWin, streakCurr, streakMax);
		centerItems.getChildren().add(stats);
	}

	/*
	 * Sets up the guess distribution graph section.
	 */
	private void setUpBottom() {
		bottomItems = new VBox();
		guessDistributionHeader = new Label("Guess Distribution");
		guessDistributionHeader.setFont(headerFont);

		// initialize the x and y axis into the horizontal bar chart
		xAxis = new NumberAxis();
		xAxis.setTickLabelsVisible(false);
		xAxis.setTickMarkVisible(false);
		yAxis = new CategoryAxis();

		guessDistributionChart = new BarChart<Number, String>(xAxis, yAxis);
		int[] pastWins = new int[6];
		if (curr != null) {
			pastWins = curr.getPastWins();
		}

		// add the data to the graph
		winsData = new XYChart.Series<>();
		// cycle through to add the data and appropriate labels
		for (int i = 5; i >= 0; i--) {
			XYChart.Data<Number, String> data = new Data<Number, String>(pastWins[i], (i + 1) + "");
			data.nodeProperty().addListener(new ChangeListener<Node>() {
				@Override
				public void changed(ObservableValue<? extends Node> arg0, Node arg1, Node arg2) {
					setNodeStyle(data);
					displayDataLabel(data);
				}
			});
			winsData.getData().add(data);
		}

		guessDistributionChart.getData().add(winsData);
		guessDistributionChart.setLegendVisible(false);
		guessDistributionChart.setHorizontalGridLinesVisible(false);
		guessDistributionChart.setMaxWidth(380);
		guessDistributionChart.setVerticalGridLinesVisible(false);
		guessDistributionChart.setHorizontalZeroLineVisible(false);

		// making the bar colors green

		bottomItems.getChildren().addAll(guessDistributionHeader, guessDistributionChart);
	}

	/*
	 * Sets the bars in the bar chart to the color limegreen
	 */
	private void setNodeStyle(XYChart.Data<Number, String> data) {
		Node node = data.getNode();
		node.setStyle("-fx-bar-fill: limegreen;");
	}

	/*
	 * Sets the label above the bar on how many wins of that category the user has
	 */
	private void displayDataLabel(XYChart.Data<Number, String> data) {
		Node node = data.getNode();
		Text graphText = new Text(data.getXValue() + "");
		graphText.setFont(new Font("Arial", 13));
		node.parentProperty().addListener(new ChangeListener<Parent>() {
			@Override
			public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
				Group parentGroup = (Group) parent;
				parentGroup.getChildren().add(graphText);
			}
		});

		node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
				graphText.setLayoutX(Math.round(bounds.getMaxX() + graphText.prefWidth(-1) * 0.5));
				graphText.setLayoutY(
						Math.round(bounds.getMaxY() - bounds.getHeight() / 2 - graphText.prefHeight(-1) / 2));
			}
		});
	}

	/*
	 * Makes sure all elements are in the proper color according to light or dark
	 * mode
	 */
	private void style() {
		// general text and label change
		this.setStyle("-fx-background-color: " + backgroundColor + ";");
		gamesPlayedNumber.setStyle("-fx-text-fill: " + textColor + ";");
		winPercentNumber.setStyle("-fx-text-fill: " + textColor + ";");
		currentStreakNumber.setStyle("-fx-text-fill: " + textColor + ";");
		maxStreakNumber.setStyle("-fx-text-fill: " + textColor + ";");
		gamesPlayed.setStyle("-fx-text-fill: " + textColor + ";");
		winPercent.setStyle("-fx-text-fill: " + textColor + ";");
		currentStreak.setStyle("-fx-text-fill: " + textColor + ";");
		maxStreak.setStyle("-fx-text-fill: " + textColor + ";");
		welcomeHeader.setStyle("-fx-text-fill: " + textColor + ";");
		statisticsHeader.setStyle("-fx-text-fill: " + textColor + ";");
		guessDistributionHeader.setStyle("-fx-text-fill: " + textColor + ";");

		// graph style update
		yAxis.setStyle("-fx-text-fill: " + textColor + ";");
		xAxis.setStyle("-fx-text-fill: " + textColor + ";");
		// update tick labels
		if (textColor.equals("black")) {
			yAxis.setTickLabelFill(Color.BLACK);
		} else {
			yAxis.setTickLabelFill(Color.WHITE);
		}
		guessDistributionChart.lookup(".chart-plot-background")
				.setStyle("-fx-background-color: " + backgroundColor + ";");

		// update the graph label inside the chart
		for (XYChart.Data<Number, String> data : winsData.getData()) {
			Node node = data.getNode();
			Group parent = (Group) node.getParent();
			for (Node child : parent.getChildren()) {
				if (child instanceof Text) {
					if (textColor.equals("black")) {
						((Text) child).setFill(Color.BLACK);
					} else {
						((Text) child).setFill(Color.WHITE);
					}
				}
			}
		}
	}
}
