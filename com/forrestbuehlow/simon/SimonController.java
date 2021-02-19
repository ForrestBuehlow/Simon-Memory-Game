package com.forrestbuehlow.simon;

public class SimonController {

	private Simon simonGame;
	private JButton[] buttons;
	private ArrayDeque<Color> playerInput;

	public enum Mode {
		INPUT,
		PLAYBACK
	}

	public SimonController(JButton[] buttons) {
		this.buttons = buttons;
		simonGame = new Simon(Simon.Mode.NORMAL);
	}

	public setGameMode(Simon.Mode mode) {

	}

}
