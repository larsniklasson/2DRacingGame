package edu.chl._2DRacingGame.screens;

/**
 * A class that makes it possible to resume the game if it is paused
 * @author Anton Ingvarsson
 */
public interface PauseScreenListener extends MainMenuDisplayer {
    void resumeRace();
}
