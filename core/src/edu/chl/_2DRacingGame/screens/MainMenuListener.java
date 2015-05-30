package edu.chl._2DRacingGame.screens;

/**
 * A class which displays the correct screen or exits the game
 *
 * @author Anton Ingvarsson
 */
public interface MainMenuListener {
    void displaySinglePlayerMenuScreen();
    void displayMultiPlayerMenuScreen();
    void displayOptionsScreen();
    void exitGame();
    void displayControlsMenuScreen();
}
