package tests;

/**
 * This test file is no longer necessary
 */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;
import model.LetterState;

class LetterStateTest {

	@Test
	void test() {
		LetterState c1 = LetterState.GREEN;
		LetterState c2 = LetterState.GRAY;
		LetterState c3 = LetterState.YELLOW;
		
		assertEquals(Color.GREEN, c1.getValue());
		assertEquals(Color.GRAY, c2.getValue());
		assertEquals(Color.YELLOW, c3.getValue());
	}

}
