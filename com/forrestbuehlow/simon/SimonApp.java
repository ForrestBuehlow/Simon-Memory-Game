package com.forrestbuehlow.simon;

import javax.swing.*;
import javax.swing.plaf.basic.*;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayDeque;

public class SimonApp {
	
	// Game components
	private static Simon game;
	private static ArrayDeque<Color> playerInput;
	private static int currentScore;
	private static int highScore;

	// Panel
	private static JPanel panel;

	// Frame
	private static JFrame frame;

	// Frame buttons
	private static JButton[] buttons;

	// Frame Menu items
	private static JMenuBar menuBar;
	private static JMenu gameModeMenu;
	private static JMenuItem startMenuButton;
	private static JCheckBoxMenuItem shrunkSequenceMenuItem, normalModeMenuItem;
	private static JTextField highScoreLabel, currentScoreLabel;

	/**
	 * The main application. Creates all components necessary to play the game of Simon on a gui.
	 */
	private static void createAndShowGUI() {

		// Initialize game components
		game = new Simon(Simon.Mode.NORMAL);
		playerInput = new ArrayDeque<>();
		currentScore = 0;
		highScore = 0;

		// Initialize the main window
		frame = new JFrame("Simon Says:");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initialize the menu & menu components
		menuBar = new JMenuBar();
		gameModeMenu = new JMenu("Game Mode");
		normalModeMenuItem = new JCheckBoxMenuItem("Normal Mode");
		shrunkSequenceMenuItem = new JCheckBoxMenuItem("Shrunken Sequence Mode");
		startMenuButton = new JMenuItem("Start");
		highScoreLabel = new JTextField(String.format("High Score: %d", highScore));
		currentScoreLabel = new JTextField(String.format("Current Score: %d", currentScore));

		// Set configuations for menu & menu components
		highScoreLabel.setEnabled(false);
		currentScoreLabel.setEnabled(false);
		normalModeMenuItem.setState(true);
		highScoreLabel.setDisabledTextColor(java.awt.Color.BLACK);
		currentScoreLabel.setDisabledTextColor(java.awt.Color.BLACK);
		
		// Add menu components to menu bar
		menuBar.add(gameModeMenu);
		menuBar.add(startMenuButton);
		menuBar.add(highScoreLabel);
		menuBar.add(currentScoreLabel);
		gameModeMenu.add(normalModeMenuItem);
		gameModeMenu.add(shrunkSequenceMenuItem);

		// Add menu bar to frame
		frame.setJMenuBar(menuBar);

		// Set up panel
		panel = new JPanel(new GridLayout(0, 2, 25, 25));

		// Panel and frame configuration
		panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		frame.setSize(750, 750);
		frame.setContentPane(panel);
		frame.setPreferredSize(new Dimension(500, 500));
		frame.setResizable(false);
		frame.getContentPane().setBackground(new java.awt.Color(153, 153, 153));

		// Initialize, configure, and add buttons to the panel
		buttons = new JButton[4];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(new String((char)('W' + i) + ""));
			buttons[i].setContentAreaFilled(true);
			buttons[i].setOpaque(true);
			buttons[i].setBorderPainted(false);
			buttons[i].setForeground(java.awt.Color.BLACK);
			buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
			buttons[i].setUI(new BasicToggleButtonUI());
			panel.add(buttons[i]);
		}

		// Set button colors & names
		resetColors(); // Sets colors for buttons 0, 1, 2, 3
		buttons[0].setName(Color.VERMILLION.toString());
		buttons[1].setName(Color.BLUISH_GREEN.toString());
		buttons[2].setName(Color.YELLOW.toString());
		buttons[3].setName(Color.BLUE.toString());

		// Set Button listeners
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].addActionListener(buttonActionListener(buttons[i]));
		}

		// Listen for clicks on Start/Restart button
		startMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				currentScore = 0;
				updateScore();

				if (startMenuButton.getText().equals("Start")) {
					startMenuButton.setText("Restart");
				}
				else {
					game.clearSequence();
				}

				playNewSequence();
			}
		});

		// If the user ticks the unticked submenu item, change the gamemode that maps to that 
		// submenu button, otherwise do nothing
		shrunkSequenceMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (shrunkSequenceMenuItem.getState()) {
					shrunkSequenceMenuItem.setState(true);
					normalModeMenuItem.setState(false);
					startMenuButton.setText("Start");
					game.setMode(Simon.Mode.SHRINK);
				}
				else {
					shrunkSequenceMenuItem.setState(!shrunkSequenceMenuItem.getState());
				}
			}
		});

		// If the user ticks the unticked submenu item, change the gamemode that maps to that 
		// submenu button, otherwise do nothing
		normalModeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (normalModeMenuItem.getState()) {
					normalModeMenuItem.setState(true);
					shrunkSequenceMenuItem.setState(false);
					startMenuButton.setText("Start");
					game.setMode(Simon.Mode.NORMAL);
				}
				else {
					normalModeMenuItem.setState(!normalModeMenuItem.getState());
				}
			}
		});
		
		// Display the window
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Returns a new ActionListener for a button used in the Simon game. The pressed button is recorded,
	 * and checked against the games sequence for correctness. If the user pressed the incorrect 
	 * button, the game is reset. If the user pressed the correct button, the button either lights 
	 * up or the user pressed the last button in the sequence, in which the score is updated and 
	 * the next sequence is played
	 * 
	 * @param b the button to add a listener for
	 * @return an ActionListener object for the button given
	 */
	private static ActionListener buttonActionListener(JButton b) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (b.isEnabled()) {
					enableAllComponents(false);
					playerInput.add(Color.valueOf(b.getName()));
					spawnButtonThread(b);
					validateInput();

					enableAllComponents(true);
				}
			}
		};
	}

	/**
	 * Spawn a thread for the given button, allowing it to "light up" for a short period of time 
	 * without halting/preventing other buttons to be pressed.
	 * 
	 * @param b the button to spawn a thread for
	 */
	private static void spawnButtonThread(JButton b) {
		new Thread() {
			public void run() {
				java.awt.Color bg = b.getBackground();

				try {
					synchronized(b) {
						while (!b.getBackground().equals(bg)) {
							b.wait();
						}
						b.setBackground(bg.brighter());
						Thread.sleep(350);
						b.setBackground(bg);
						Thread.sleep(250);
						b.notify();
					}
				} catch (InterruptedException e) {
					System.err.println(e);
				}
			}
		}.start();
	}

	/**
	 * Validates the current user input against the games current sequence. If the players input is
	 * correct but not complete, the game continues. If the player inputs the last correct button in 
	 * the sequence, the score is updated and a new sequence plays. Otherwise, the player has lost
	 * and the new sequence plays.
	 */
	private static void validateInput() {
		if (playerInput.size() < game.sequenceLength() && game.startsWith(playerInput)) {
			System.out.println("Input matches so far...");
			return;
		}
		else if (playerInput.size() >= game.sequenceLength() && game.matchesSequence(playerInput)) {

			currentScore += 1;
			if (currentScore > highScore) {
				highScore = currentScore;
			}
			updateScore();
			playerInput.clear();
			playNewSequence();
		}
		else {
			JOptionPane.showMessageDialog(null, "You Lost :(");
			System.out.println("Game ended");
			currentScore = 0;
			updateScore();
			playerInput.clear();
			game.clearSequence();
			startMenuButton.setText("Start");
		}
	}

	/**
	 * Updates the labels on the menu bar to the current scores tracked.
	 */
	private static void updateScore() {
		highScoreLabel.setText(String.format("High Score: %d", highScore));
		currentScoreLabel.setText(String.format("Current Score: %d", currentScore));
	}

	/**
	 * Enables (or disables) the relevent component items on the frame. Used when the sequence is 
	 * playing back to the user so that the spawn thread continues without being uninterrupted.
	 * 
	 * @param b the flag to either enable or disable relevent components
	 */
	private static void enableAllComponents(boolean b) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setEnabled(b);
		}
		menuBar.setEnabled(b);
		gameModeMenu.setEnabled(b);
		startMenuButton.setEnabled(b);
		shrunkSequenceMenuItem.setEnabled(b);
		normalModeMenuItem.setEnabled(b);
	}

	/**
	 * Plays the sequence generated from the Simon game.
	 */
	private static void playNewSequence() {
		SimonApp.game.generateColor();

		new Thread() {
			public void run() {
				enableAllComponents(false);
				Color[] sequence = SimonApp.game.getSequence();
				// System.out.println(java.util.Arrays.toString(sequence));
				try {
					Thread.sleep(500);
					for (Color s : sequence) {
						int i = 0;
						switch (s) {
							case VERMILLION:
								i = 0;
								break;
							case BLUISH_GREEN:
								i = 1;
								break;
							case YELLOW:
								i = 2;
								break;
							case BLUE:
								i = 3;
								break;
						}
						synchronized(buttons) {
							java.awt.Color bg = buttons[i].getBackground();
							Thread.sleep(350);
							buttons[i].setBackground(bg.brighter());
							Thread.sleep(250);
							buttons[i].setBackground(bg);
							resetColors();
						}
					}
					enableAllComponents(true);
				} catch (InterruptedException e) {
					System.err.println(e);
				}
			}
		}.start();
	}

	/**
	 * Resets the colors of the buttons to their defaults. Learning Java threads and concurrency made
	 * this a required component of the code
	 */
	private static void resetColors() {
		buttons[0].setBackground(new java.awt.Color(213, 94, 0)); 
		buttons[1].setBackground(new java.awt.Color(0, 158, 115));
		buttons[2].setBackground(new java.awt.Color(240, 228, 66));
		buttons[3].setBackground(new java.awt.Color(0, 114, 178));
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
