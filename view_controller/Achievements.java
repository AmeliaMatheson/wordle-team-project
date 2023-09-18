package view_controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.WordleBackend;

/**
 * Pane displaying the achievements of the current player. There are 10 different achievements
 * with images representing them. If the player does not have the achievement a black and white version
 * is displayed until that achievement is made.
 * 
 * 
 * @author Blue Garrabrant
 * @since May 1, 2023
 */

public class Achievements extends ScrollPane{ 
	
	private WordleBackend backend;
	
	private VBox all = new VBox(30);
	
	private HBox row0 = new HBox(30);
	private HBox row1 = new HBox(30);
	private HBox row2 = new HBox(30);
	private HBox row3 = new HBox(30);
	private HBox row4 = new HBox(30);
	private HBox row5 = new HBox(30);
	private HBox row6 = new HBox(30);
	
	private ImageView firstWin = new ImageView();  
	private ImageView win5 = new ImageView();  
	private ImageView win10 = new ImageView();  
	private ImageView win50 = new ImageView(); 
	private ImageView win100 = new ImageView();  
	private ImageView streak5 = new ImageView();  
	private ImageView streak10 = new ImageView();  
	private ImageView streak30 = new ImageView();  
	private ImageView firstGuess = new ImageView();  
	private ImageView oneOfEach = new ImageView(); 
	
	private Image firstWinFalse;  
	private Image win5False;  
	private Image win10False;  
	private Image win50False; 
	private Image win100False;  
	private Image streak5False;  
	private Image streak10False;  
	private Image streak30False;  
	private Image firstGuessFalse;  
	private Image oneOfEachFalse; 
	
	private Image firstWinTrue;  
	private Image win5True;  
	private Image win10True;  
	private Image win50True; 
	private Image win100True;  
	private Image streak5True;  
	private Image streak10True;  
	private Image streak30True;  
	private Image firstGuessTrue;  
	private Image oneOfEachTrue; 
	
	private int mode = 1;

	public Achievements(WordleBackend givenBackend) throws FileNotFoundException {
		backend = givenBackend;
		setImages();
		this.reset();
		
		this.setContent(all);
		
		row0.setMinHeight(10);
		row6.setMinHeight(10);

		row1.setAlignment(Pos.CENTER);
		row2.setAlignment(Pos.CENTER);
		row3.setAlignment(Pos.CENTER);
		row4.setAlignment(Pos.CENTER);
		row5.setAlignment(Pos.CENTER);

        row1.getChildren().addAll(firstWin, win5);
        row2.getChildren().addAll(win10, win50);
        row3.getChildren().addAll(win100, streak5);
        row4.getChildren().addAll(streak10, streak30);
        row5.getChildren().addAll(firstGuess, oneOfEach);
        
        all.getChildren().addAll(row0, row1, row2, row3, row4, row5, row6);

	}
	
	/**
	 * Sets all of the images with digital drawn images for each achievement.
	 * There are grayed out version if the player has not made that achievement yet.
	 * 
	 * @throws FileNotFoundException
	 */
	public void setImages() throws FileNotFoundException {
		firstWinFalse = new Image(new FileInputStream("firstWinFalse.png"));  	
		win5False = new Image(new FileInputStream("win5False.png"));
		win10False = new Image(new FileInputStream("win10False.png"));
		win50False = new Image(new FileInputStream("win50False.png"));  
		win100False = new Image(new FileInputStream("win100False.png"));
		streak5False = new Image(new FileInputStream("streak5False.png"));  
		streak10False = new Image(new FileInputStream("streak10False.png"));
		streak30False = new Image(new FileInputStream("streak30False.png"));  
		firstGuessFalse = new Image(new FileInputStream("firstGuessFalse.png"));
		oneOfEachFalse = new Image(new FileInputStream("oneOfEachFalse.png"));
		firstWinTrue = new Image(new FileInputStream("firstWinTrue.png"));  
		win5True = new Image(new FileInputStream("win5True.png"));
		win10True = new Image(new FileInputStream("win10True.png"));
		win50True = new Image(new FileInputStream("win50True.png"));  
		win100True = new Image(new FileInputStream("win100True.png"));
		streak5True = new Image(new FileInputStream("streak5True.png"));  
		streak10True = new Image(new FileInputStream("streak10True.png"));
		streak30True = new Image(new FileInputStream("streak30True.png"));  
		firstGuessTrue = new Image(new FileInputStream("firstGuessTrue.png"));
		oneOfEachTrue = new Image(new FileInputStream("oneOfEachTrue.png"));
	}
	
	/**
	 * Sets all images to grayed out images when the player is logged out.
	 * 
	 */
	public void reset() {
		firstWin.setImage(firstWinFalse);
		win5.setImage(win5False); 
		win10.setImage(win10False); 
		win50.setImage(win50False);
		win100.setImage(win100False);
		streak5.setImage(streak5False);
		streak10.setImage(streak10False);
		streak30.setImage(streak30False);
		firstGuess.setImage(firstGuessFalse);
		oneOfEach.setImage(oneOfEachFalse);
		firstWin.setFitWidth(175);
		win5.setFitWidth(175);
		win10.setFitWidth(175);
		win50.setFitWidth(175);
		win100.setFitWidth(175);
		streak5.setFitWidth(175);
		streak10.setFitWidth(175);
		streak30.setFitWidth(175);
		firstGuess.setFitWidth(175);
		oneOfEach.setFitWidth(175);
		firstWin.setFitHeight(175);
		win5.setFitHeight(175);
		win10.setFitHeight(175);
		win50.setFitHeight(175);
		win100.setFitHeight(175);
		streak5.setFitHeight(175);
		streak10.setFitHeight(175);
		streak30.setFitHeight(175);
		firstGuess.setFitHeight(175);
		oneOfEach.setFitHeight(175);
	}
	
	/**
	 * Switch between light and dark mode.
	 */
	public void changeMode() {
		if (mode == 1) {
			this.setStyle("-fx-background: " + "#303030" + "; -fx-border-color: " + "#303030" + ";");
		} else {
			this.setStyle("-fx-background: " + "white" + "; -fx-border-color: " + "white" + ";");
		}
		mode *= -1;
	}
	
	
	/**
	 * Updates achievements status of the current player.
	 */
	public void update() {
		if(backend.getCurrPlayer() == null) {
			reset();
			return;
		}
		boolean[] achievements = backend.getCurrPlayer().getAchievements();
		if (achievements[0]) {
			firstWin.setImage(firstWinTrue);
		}
		if (achievements[1]) {
			win5.setImage(win5True);
		}
		if (achievements[2]) {
			win10.setImage(win10True);
		}
		if (achievements[3]) {
			win50.setImage(win50True);
		}
		if (achievements[4]) {
			win100.setImage(win100True);
		}
		if (achievements[5]) {
			streak5.setImage(streak5True);
		}
		if (achievements[6]) {
			streak10.setImage(streak10True);
		}
		if (achievements[7]) {
			streak30.setImage(streak30True);
		}
		if (achievements[8]) {
			firstGuess.setImage(firstGuessTrue);
		}
		if (achievements[9]) {
			oneOfEach.setImage(oneOfEachTrue);
		}
	}

}
