package edu.chl._2DRacingGame.screens;

/**
 * A class able to restarts or exits the game
 * @author Anton Ingvarsson
 */
public interface SinglePlayerFinishedScreenListener extends MainMenuDisplayer {
    void restartRace();
    void exitGame();
}
