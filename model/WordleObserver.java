package model;

/**
 * Observers to the model of the Wordle game must implement this interface.
 * 
 * @author Adrianna Koppes, with much code adapted from Rick Mercer
 * @since April 9, 2023
 */
public interface WordleObserver {
	/**
	 * Updates the view to the current state.
	 * 
	 * @param observed : WordleObservable object representing the model that changed
	 *                 state.
	 */
	void update(WordleObservable observed);
}
