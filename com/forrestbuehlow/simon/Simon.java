package com.forrestbuehlow.simon;

import java.util.Random;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;

/**
 * A digital representation of the electronic game of memory skill "Simon" invented by Ralph H. Baer
 * and Howard J. Morrison (https://en.wikipedia.org/wiki/Simon_(game))
 */
public class Simon {

	/**
	 * Indicates the mode for the current game of Simon.
	 * 
	 * NORMAL - A continuous sequence of colors are generated in random order
	 * SHRINK - Like normal, but with no duplicate pairs in the sequence.
	 * 
	 * @see com.forrestbuehlow.simon.Color for the supported colors in this iteration
	 */
	public enum Mode {
		NORMAL, // Next instruction in sequence is random
		SHRINK // Next instruction in sequence is not equal to the last -- no duplicate pairs
	}

	/**
	 * Indicates the mode for the current game of Simon.
	 * 
	 * NORMAL - A continuous sequence of colors are generated in random order
	 * SHRINK - Like normal, but with no duplicate pairs in the sequence.
	 * 
	 * @see com.forrestbuehlow.simon.Color for the supported colors in this iteration
	 */
	private Mode mode;

	/**
	 * The current sequence of colors the game has generated
	 */
	private ArrayDeque<Color> sequence;

	/**
	 * Constructor takes a Simon.Mode to dictate how the sequence will be generated.
	 * 
	 * @throws RuntimeException if the mode is SHRINK, but there's less than 2 colors in Color
	 * @param mode The mode for the sequence generation
	 */
	public Simon(Mode mode) {
		if (mode.equals(Mode.SHRINK) && Color.values().length < 2) {
			throw new RuntimeException("Mode.SHRINK not compatible with Color lengths < 2");
		}
		this.mode = mode;
		sequence = new ArrayDeque<>();
	}

	/**
	 * Change the game mode, reseting the current sequence.
	 * 
	 * @param mode the desired game mode to play
	 */
	public void setMode(Mode mode) {
		if (this.mode == mode) {
			return;
		}
		sequence.clear();
		this.mode = mode;
	}

	/**
	 * The current sequences length
	 * 
	 * @return the length of the current sequence
	 */
	public int sequenceLength() {
		return sequence.size();
	}

	/**
	 * Checks if the given Collection<Color> object matches the current sequence. Returns false if 
	 * the sizes of the sequence and the given Collection are not equal, or if the ordering of Colors
	 * do not match one another. Returns true otherwise
	 * 
	 * @param c the collection to compare
	 * @return true if the given collection is a complete match of the sequence, false otherwise
	 */
	public boolean matchesSequence(Collection<Color> c) {
		if (sequence.size() != c.size()) {
			return false;
		}

		Iterator<Color> thisItt = sequence.iterator();
		Iterator<Color> otherItt = c.iterator();

		while (thisItt.hasNext()) {
			if (!thisItt.next().equals(otherItt.next())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the given Collection<Color> is a subset of the current sequence, starting from the 
	 * first element.
	 * 
	 * @param c the collection to compare
	 * @return true if c is a subset from the first element, false otherwise
	 */
	public boolean startsWith(Collection<Color> c) {
		if (c.size() > sequence.size()) {
			return false;
		}
		Iterator<Color> thisItt = sequence.iterator();
		Iterator<Color> otherItt = c.iterator();

		while (otherItt.hasNext()) {
			if (!otherItt.next().equals(thisItt.next())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Clears the current sequence
	 */
	public void clearSequence() {
		sequence.clear();
	}

	/**
	 * Returns the current sequence as a primitive Color array
	 * 
	 * @return a primitive array of Colors
	 */
	public Color[] getSequence() {
		return sequence.toArray(new Color[0]);
	}

	/**
	 * Generates a new color to add to the sequence. If the current game mode is normal, any Color 
	 * is selected at random. Otherwise if the game mode is Shrink, then a color is continiously 
	 * generated until it differs from the last Color in the sequence. Adds the generated Color
	 * to the sequence
	 * 
	 * @return the generated Color
	 */
	public Color generateColor() {
		Random r = new Random();
		Color[] colors = Color.values();

		// By default, assume mode is normal
		Color nextColor = colors[r.nextInt(colors.length)];

		if (mode.equals(Mode.SHRINK)) {
			while (nextColor.equals(sequence.peekLast())) {
				nextColor = colors[r.nextInt(colors.length)];
			}
		}
		sequence.addLast(nextColor);
		return nextColor;
	}
	
}
