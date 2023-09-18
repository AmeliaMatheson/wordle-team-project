package view_controller;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.WordleBackend;


/**
 * Pane to log in the player. First asks if play is logging in or creating an account.
 * Notifies player is there is an error logging in or creating account. Displayed when
 * game is first opened or when player logs out.
 * 
 * @author Blue Garrabrant
 * @since April 19, 2023
 */
public class LogInPane extends VBox{

	private Label welcomeLabel = new Label("Please Log In Or Create A New Account");
	private Label userLabel = new Label("Username");
	private Label passwordLabel = new Label("Password");
	private TextField usernameTextField = new TextField();
	private PasswordField passwordTextField = new PasswordField();
	private Button logInSwitch = new Button("Log In");
	private Button createNewAccountSwitch = new Button("Create New Account");
	private Button logIn = new Button("Log In");
	private Button createAccount = new Button("Create Account");
	private Button goBack = new Button("Back");


	private HBox oldOrNewButtons = new HBox(30);
	private HBox username = new HBox(30);
	private HBox password = new HBox(32);
	
	private WordleBackend backend;
	
	private int mode = 1;
	private String color = "white";
	private String text = "black";
	
	public LogInPane(WordleBackend givenBackend) {
		backend = givenBackend;
		this.setSpacing(10);
		this.setPadding(new Insets(10));

        oldOrNewButtons.getChildren().addAll(logInSwitch,createNewAccountSwitch);
        username.getChildren().addAll(userLabel, usernameTextField);
        password.getChildren().addAll(passwordLabel, passwordTextField);
        
		// show buttons for user to choose to log in or create account
		this.getChildren().clear();
        this.getChildren().addAll(welcomeLabel, oldOrNewButtons);
        
        setHandlers();
	}
	
	/**
	 * Switch between light and dark mode.
	 */
	public void changeMode() {
		if (mode == 1) {
			color = "Dimgrey";
			text = "White";

		} else {
			color = "White";
			text = "Black";
		}
		
		usernameTextField.setStyle("-fx-control-inner-background: " + color + ";");
		passwordTextField.setStyle("-fx-control-inner-background: " + color + ";");
		logInSwitch.setStyle("-fx-base: " + color + ";");
		createNewAccountSwitch.setStyle("-fx-base: " + color + ";");
		logIn.setStyle("-fx-base: " + color + ";");
		createAccount.setStyle("-fx-base: " + color + ";");
		goBack.setStyle("-fx-base: " + color + ";");
		
		welcomeLabel.setStyle("-fx-text-fill: " + text + ";");
		userLabel.setStyle("-fx-text-fill: " + text + ";");
		passwordLabel.setStyle("-fx-text-fill: " + text + ";");
		
		mode *= -1;
	}
	
	/**
	 * Warns player to log in if they want to play or view statistics.
	 * 
	 * @param tryingToOpen String representing data or functionality player is trying to access
	 */
	public void needToLogIn(String tryingToOpen) {
		if (tryingToOpen.equals("Log Out")) {
			welcomeLabel.setText(("Please Log In Or Create A New Account"));
		} else {
			welcomeLabel.setText("You Need To Log In To Access " + tryingToOpen);
		}
		this.getChildren().clear();
        this.getChildren().addAll(welcomeLabel, oldOrNewButtons);
	}
	

	private void setHandlers() {
		// show elements necessary to log in
		logInSwitch.setOnAction(event -> {
			welcomeLabel.setText("Please Log In To Play");
			this.getChildren().clear();
	        this.getChildren().addAll(welcomeLabel, username, password, logIn, goBack);
	        usernameTextField.setText("");
	        passwordTextField.setText("");

		});
		
		// show elements necessary to create account
		createNewAccountSwitch.setOnAction(event -> {
			welcomeLabel.setText("Please Create An Account To Play");
			this.getChildren().clear();
	        this.getChildren().addAll(welcomeLabel, username, password, createAccount, goBack);
	        usernameTextField.setText("");
	        passwordTextField.setText("");
		});
		
		// show buttons for user to choose to log in or create account
		goBack.setOnAction(event -> {
			welcomeLabel.setText("Please Log In Or Create A New Account");
			this.getChildren().clear();
	        this.getChildren().addAll(welcomeLabel, oldOrNewButtons);
		});
		
		// log in the player
		logIn.setOnAction(event -> {
			if (!backend.login(usernameTextField.getText(), passwordTextField.getText())) {
				welcomeLabel.setText("Username Or Password Is Incorrect.  Please Try Again.");
			} else {
				welcomeLabel.setText("Hello " + usernameTextField.getText() +  "! You Are Now Logged In.");
				this.getChildren().clear();
		        this.getChildren().addAll(welcomeLabel);
			}
		});
		
		// create account and log in the player
		createAccount.setOnAction(event -> {
			if(usernameTextField.getText().trim().equals("") || 
					passwordTextField.getText().trim().equals("")) {
				welcomeLabel.setText("Please Enter A Username And Password.");
				return;
			}
			else if (!backend.createAccount(usernameTextField.getText(), passwordTextField.getText())) {
				welcomeLabel.setText("That Username Is Already Taken.  Please Try Again.");
				return;
			}
			else {
				welcomeLabel.setText("Hello " + usernameTextField.getText() +  "!  You Are Now Logged In.");
				this.getChildren().clear();
		        this.getChildren().addAll(welcomeLabel);
			}
		});
	}
}