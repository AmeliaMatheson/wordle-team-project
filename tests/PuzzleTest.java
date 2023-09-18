package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Puzzle;
import model.WordList;

class PuzzleTest {

	@Test
	void getPuzzleWordTest() {
		Puzzle puzzle = new Puzzle();
		assertFalse(puzzle.getWord() == null);
	}
	
	@Test
	void guessTest() {
		System.out.println("0x808080ff = gray(incorrect)\n 0xffff00ff = yellow(inWord)\n 0x008000ff = green(correct)\n");
		Puzzle puzz1 = new Puzzle();
		System.out.println("Puzzle word: " + puzz1.getWord());
		assertTrue(puzz1.guess("uehubf") == null);
		System.out.println(puzz1.guess("hello").toString());
		
		Puzzle puzz2 = new Puzzle();
		System.out.println("Puzzle 2 word: " + puzz2.getWord());
		assertFalse(puzz2.guess("peace") == null);
		System.out.println(puzz2.guess("peace").toString());
		
		Puzzle puzz3 = new Puzzle();
		System.out.println("Puzzle 3 word: " + puzz3.getWord());
		assertTrue(puzz3.guess("cents") == null);
		assertTrue(puzz3.guess("cat") == null);
		System.out.println(puzz3.guess("rainy").toString());
		
		Puzzle puzz4 = new Puzzle();
		System.out.println("Puzzle 4 word: " + puzz4.getWord());
		assertTrue(puzz4.guess("attempt") == null);
		System.out.println(puzz4.guess("chair").toString());
	}

}
