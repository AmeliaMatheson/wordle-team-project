package model;

import java.util.ArrayList;

/**
 * Notifies observers of any change in state of the Wordle game.
 * 
 * @author Adrianna Koppes, with much code adapted from Rick Mercer
 * @since April 9, 2023
 */
public class WordleObservable {
	ArrayList<WordleObserver> views = new ArrayList<>();

	/**
	 * Adds a new observer to this model.
	 * 
	 * @param toAdd : WordleObserver representing a new view to be added.
	 */
	public void add(WordleObserver toAdd) {
		views.add(toAdd);
	}

	/**
	 * Updates all of the views.
	 * 
	 * @param observable : WordleObservable object representing the model that holds
	 *                   the current state.
	 */
	public void notifyObservers(WordleObservable observable) {
		for (WordleObserver view : views) {
			view.update(observable);
		}
	}
}
