# Game of "Simon" simulation

This program simulates the electronic memory game of memory skill "Simon" invented by Ralph H. Baer 
and Howard J. Morrison (see: https://en.wikipedia.org/wiki/Simon_(game))

This simulation of "Simon" uses accessible color pallets for its buttons.

## Purpose

This was created for the use of a debugging workshop hosted on February 22, 2021 for Carleton 
University students enrolled in COMP2402 at the time, to highlight the benefits of using a debugging 
tool. While bugs were purposely introduced into a version of this code for the purpose described,
this repository does not represent that version. That is, any bugs in this current version of the 
project is not intended, and present due to the time constraint and inexperience with Java threads.

## How to Compile and Run

Compile with `javac com/forrestbuehlow/simon/SimonApp.java` and run with `java com.forrestbuehlow.simon.SimonApp` 
from the directory that contains the `com` directory.

## How to Play

This simulation has two modes: "Normal Mode" & "Shrunken Sequence Mode". By default, "Normal Mode" is 
set on launch. To change the game mode, select the desired game mode from the drop-down menu item in 
"Game Mode".

"Normal Mode" generates a sequence of colors the player must repeat in the same order as they 
appeared, increasing in length with each successful sequence repeated. The increasing sequence is the 
same sequence the user would have seen previously, with a new color added to the end of that sequence.

"Shrunken Sequence Mode" is much like "Normal Mode", except that no pair in the sequence will have 
the same color. That is, `Y, B, Y, R` is a valid sequence in this mode, but `Y, Y, B, R` is not.

The program tracks the current score, where a score is the number of sequences repeated by the user 
in a row, and the high score, the highest score achieved by the user. The current score (and the current 
sequence) will reset if the game mode is changed. Both high score and current score are lost when 
the program closes. Both scores are found in the program’s menu bar.

Press the "Start" button to start the game in the current game mode. The four buttons in the program 
window will light up in a specific order, in which the user must repeat. If the user-repeated sequence 
is correct, the next sequence will play, otherwise if the user makes a mistake, the game will stop 
and the user must "Restart" (previously titled "Start" in the menu bar).

## Known Bugs
This project is low priority due to its purpose. Otherwise, here are the current known bugs:

- [ ] Colors sometimes don't reset to their base values after lighting up
- [X] Button gives no feedback if the button pressed is the last (correct) Color in the sequence. This 
was to overcome the issue of there being no delay between the end of one sequence and the beginning of 
another, which makes it difficult to identify the first color in the new sequence.

## License
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
